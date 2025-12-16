package br.edu.ifpb.smh.services;

import br.edu.ifpb.smh.dto.ConsumoAtualDTO;
import br.edu.ifpb.smh.dto.HistoricoConsumoDTO;
import br.edu.ifpb.smh.dto.SHA_DTO;
import br.edu.ifpb.smh.hardware.AdapterSimuladorUnico;
import br.edu.ifpb.smh.hardware.LeitorSHA;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SensorService {

    private final Tesseract tesseract;
    private final AlertaService alertaService;
    private final List<LeitorSHA> leitoresConfigurados = new ArrayList<>();

    // Simulação de Banco de Dados
    private final Map<String, Map<Date, Double>> repositorioLeituras = new HashMap<>();

    public SensorService(AlertaService alertaService) {
        this.alertaService = alertaService;
        this.tesseract = new Tesseract();

        // AJUSTE O CAMINHO SE NECESSÁRIO
        this.tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        this.tesseract.setLanguage("eng");

        // --- CORREÇÃO 1: MUDANÇA DE MODO ---
        // Mode 7 era "Single Line" (Linha única). Sua imagem tem várias linhas.
        // Mode 3 é "Auto Segmentation" (Detecta blocos de texto automaticamente).
        this.tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_AUTO);

        // Define apenas caracteres numéricos na whitelist para evitar ler "Hidrometro" como texto
        // Isso ajuda muito a evitar ler "Se" ou "ABC"
        this.tesseract.setTessVariable("tessedit_char_whitelist", "0123456789");

        configurarSimuladores();
    }

    public SensorService() {
        this(null);
    }

    private void configurarSimuladores() {
        // --- SEU SIMULADOR ---
        leitoresConfigurados.add(new AdapterSimuladorUnico(
                "SHA-ARTHUR-01",
                "C:\\dev\\padroes-de-projeto\\hidrometro_saida.png"
        ));
    }

    public void iniciarVarredura() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            for (LeitorSHA leitor : leitoresConfigurados) {
                leitor.processar(this);
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    public void processarImagemOCR(String shaId, File arquivoImagem) {
        try {
            BufferedImage original = ImageIO.read(arquivoImagem);
            if (original == null) return;

            // --- CORREÇÃO 2: RECORTE ESTRATÉGICO ---
            // Analisando sua imagem 'hidrometro_saida.png':
            // O número '000183' está no meio. O cabeçalho tem o serial '12345' que atrapalha.
            // Vamos pegar uma faixa central da imagem.
            // Pula os primeiros 25% (cabeçalho) e pega 35% de altura (onde está o número gigante)
            int y = (int) (original.getHeight() * 0.25);
            int h = (int) (original.getHeight() * 0.35);
            int w = original.getWidth();

            BufferedImage recortada = original.getSubimage(0, y, w, h);

            // --- CORREÇÃO 3: ZOOM DIGITAL (ESCALA) ---
            // O Tesseract erra muito em imagens pequenas. Vamos duplicar o tamanho (2x).
            BufferedImage imagemFinal = escalarImagem(recortada, 2.0);

            // Debug visual (Opcional: Salva o que o OCR vai ler na pasta do projeto para vc conferir)
            // ImageIO.write(imagemFinal, "png", new File("debug_ocr_" + shaId + ".png"));

            String texto = tesseract.doOCR(imagemFinal);

            // Remove quebras de linha e espaços
            String numeros = texto.replaceAll("[^0-9]", "").trim();

            System.out.println("   [DEBUG OCR] Texto Bruto: '" + texto.replace("\n", " ") + "' | Numeros: '" + numeros + "'");

            if (!numeros.isEmpty()) {
                // Conversão: O simulador mostra litros inteiros (ex: 000183 = 183 litros)
                // Convertendo para m3: 183 / 1000 = 0.183 m3
                double valor = Double.parseDouble(numeros) / 1000.0;

                registrarLeitura(shaId, valor);
            } else {
                System.out.println("   [ERRO OCR] Nenhum número identificado. Verifique a imagem.");
            }

        } catch (Exception e) {
            System.err.println("Erro Crítico OCR " + shaId + ": " + e.getMessage());
        }
    }

    // Método auxiliar para dar Zoom na imagem e facilitar a vida do OCR
    private BufferedImage escalarImagem(BufferedImage src, double fator) {
        int w = (int) (src.getWidth() * fator);
        int h = (int) (src.getHeight() * fator);
        BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(src, 0, 0, w, h, null);
        g.dispose();
        return resized;
    }

    private void registrarLeitura(String shaId, double valor) {
        repositorioLeituras.putIfAbsent(shaId, new HashMap<>());
        repositorioLeituras.get(shaId).put(new Date(), valor);

        System.out.printf(">>> LEITURA SALVA: SHA %s | Valor: %.3f m3%n", shaId, valor);

        if (alertaService != null) {
            // ID fixo do usuário criado na Main para teste
            alertaService.verificarRegras("e82bacc5", valor);
        }
    }

    // --- MÉTODOS DE CONSULTA ---

    public HistoricoConsumoDTO getConsumoPorSha(String shaId, Date inicio, Date fim) {
        Map<Date, Double> leituras = repositorioLeituras.getOrDefault(shaId, new HashMap<>());
        return new HistoricoConsumoDTO(shaId, inicio, fim, 0.0, leituras);
    }

    public HistoricoConsumoDTO getConsumoAgregado(List<SHA_DTO> shas, Date inicio, Date fim) {
        return new HistoricoConsumoDTO("Agregado", inicio, fim, 0.0, new HashMap<>());
    }

    public ConsumoAtualDTO getConsumoAtualAgregado(List<SHA_DTO> shas) {
        double total = 0;
        for (SHA_DTO sha : shas) {
            Map<Date, Double> leituras = repositorioLeituras.get(sha.getId());
            if (leituras != null && !leituras.isEmpty()) {
                total += leituras.values().stream().reduce((a, b) -> b).orElse(0.0);
            }
        }
        return new ConsumoAtualDTO("User", total, new Date(), total > 70);
    }
}