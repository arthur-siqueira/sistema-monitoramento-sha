package br.edu.ifpb.smh.services;

import br.edu.ifpb.smh.dto.*;
import br.edu.ifpb.smh.hardware.*;
import br.edu.ifpb.smh.strategy.*; // Importando as novas estratégias
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class SensorService {

    private final Tesseract tesseract;
    private final AlertaService alertaService;
    private final List<LeitorSHA> leitoresConfigurados = new ArrayList<>();

    // MAPA MÁGICO: Associa o ID do sensor à sua estratégia de OCR
    private final Map<String, OCRStrategy> estrategiasDeLeitura = new HashMap<>();

    // Simulação de Banco de Dados
    private final Map<String, Map<Date, Double>> repositorioLeituras = new HashMap<>();

    public SensorService(AlertaService alertaService) {
        this.alertaService = alertaService;
        this.tesseract = new Tesseract();
        this.tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        this.tesseract.setLanguage("eng");
        this.tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_AUTO);
        this.tesseract.setTessVariable("tessedit_char_whitelist", "0123456789");

        configurarSimuladores();
    }

    public SensorService() { this(null); }

    private void configurarSimuladores() {
        // --- CONFIGURAÇÃO DO SIMULADOR ARTHUR ---
        String idArthur = "SHA-ARTHUR-01";
        leitoresConfigurados.add(new AdapterSimuladorUnico(
                idArthur,
                "C:\\dev\\padroes-de-projeto\\hidrometro_saida.png"
        ));
        // Define que esse ID usa a estratégia do Arthur
        estrategiasDeLeitura.put(idArthur, new OCRArthurStrategy());


        // --- CONFIGURAÇÃO DO SIMULADOR CEFRAS ---
        String idCefras = "SHA-CEFRAS-01";
        leitoresConfigurados.add(new AdapterSimuladorPasta(
                idCefras,
                "C:\\dev\\Simulador-Hidrometro-Analogico_pp\\Medições_202021250024"
        ));
        // Define que esse ID usa a estratégia do Cefras
        estrategiasDeLeitura.put(idCefras, new OCRCefrasStrategy());

        // --- 3. CONFIGURAÇÃO DO SIMULADOR TÁCITO ---
        String idTacito = "SHA-TAC-01";

        // CUIDADO: Verifique se esse caminho está correto no seu PC!
        // O Tácito gera imagens em pasta (AdapterSimuladorPasta)
        leitoresConfigurados.add(new AdapterSimuladorPasta(
                idTacito,
                "C:\\dev\\Simulador-Hidrometro-Analogico\\Medições_202311250039"
        ));

        // Registra a estratégia nova
        estrategiasDeLeitura.put(idTacito, new OCRTacitoStrategy());
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

            // 1. DESCUBRA QUAL ESTRATÉGIA USAR
            OCRStrategy estrategia = estrategiasDeLeitura.get(shaId);

            if (estrategia == null) {
                System.out.println("ERRO: Nenhuma estratégia de OCR definida para " + shaId);
                return;
            }

            // 2. DELEGUE O TRABALHO SUJO PARA A ESTRATÉGIA
            String texto = estrategia.processar(tesseract, original);

            // 3. PROCESSE O RESULTADO (Igual para todos)
            String numeros = texto.replaceAll("[^0-9]", "").trim();
            System.out.println("   [DEBUG " + shaId + "] Bruto: '" + texto.replace("\n"," ") + "' | Numeros: " + numeros);

            if (!numeros.isEmpty()) {
                double valor = Double.parseDouble(numeros) / 1000.0;
                registrarLeitura(shaId, valor);
            }

        } catch (Exception e) {
            System.err.println("Erro Crítico OCR " + shaId + ": " + e.getMessage());
        }
    }

    private void registrarLeitura(String shaId, double valor) {
        repositorioLeituras.putIfAbsent(shaId, new HashMap<>());
        repositorioLeituras.get(shaId).put(new Date(), valor);
        System.out.printf(">>> LEITURA SALVA: SHA %s | Valor: %.3f m3%n", shaId, valor);

        if (alertaService != null) {
            // Nota: Num sistema real buscaríamos o ID do dono do SHA
            alertaService.verificarRegras("ID_GENERICO_TESTE", valor);
        }
    }

    // Métodos de consulta mantidos iguais...
    public HistoricoConsumoDTO getConsumoPorSha(String shaId, Date inicio, Date fim) {
        return new HistoricoConsumoDTO(shaId, inicio, fim, 0.0, repositorioLeituras.getOrDefault(shaId, new HashMap<>()));
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