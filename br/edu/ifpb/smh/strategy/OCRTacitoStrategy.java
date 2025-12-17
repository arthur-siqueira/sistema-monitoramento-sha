package br.edu.ifpb.smh.strategy;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class OCRTacitoStrategy implements OCRStrategy {

    @Override
    public String processar(Tesseract tesseract, BufferedImage original) {
        try {
            // 1. CONFIGURAÇÃO
            tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_SINGLE_LINE);
            tesseract.setTessVariable("tessedit_char_whitelist", "0123456789");

            // 2. RECORTE (CROP) - RECUPERANDO O ZERO
            // O valor 0.41 cortou o número. O valor 0.36 pegava o "B".
            // Vamos no meio termo: 0.38.
            // Isso vai incluir a barra lateral esquerda, mas vamos tratar isso via código.

            int x = (int) (original.getWidth() * 0.38); // <--- RECUADO (traz o zero de volta)
            int y = (int) (original.getHeight() * 0.37);
            int w = (int) (original.getWidth() * 0.24); // Aumentei um pouquinho para caber tudo
            int h = (int) (original.getHeight() * 0.07);

            if (x < 0) x = 0; if (y < 0) y = 0;
            if (x + w > original.getWidth()) w = original.getWidth() - x;
            if (y + h > original.getHeight()) h = original.getHeight() - y;
            if (w <= 0 || h <= 0) return "";

            BufferedImage recortada = original.getSubimage(x, y, w, h);

            // 3. ZOOM & TRATAMENTO
            BufferedImage tratada = ImageHelper.convertImageToGrayscale(recortada);
            BufferedImage finalImg = ImageHelper.getScaledInstance(tratada, tratada.getWidth() * 6, tratada.getHeight() * 6);

            // 4. EROSÃO (Ajuda a limpar as barras, mas não faz milagre sozinha)
            BufferedImage limpa = limparRuidoComErosao(finalImg);

            ImageIO.write(limpa, "png", new File("debug_visao_tacito.png"));

            // 5. LEITURA
            String resultado = tesseract.doOCR(limpa);
            String numeros = resultado.replaceAll("[^0-9]", "").trim();

            // --- LÓGICA FINAL (CORREÇÃO MATEMÁTICA) ---
            // O hidrômetro analógico padrão tem 6 dígitos pretos.
            // Como recuamos o crop, é provável que ele leia a primeira barra como '1'.
            // Exemplo: O número é "000215", mas ele lê "1000215" (Barra + Numero).

            // Regra: Se tiver mais de 6 dígitos, pegue apenas os ÚLTIMOS 6 (que são os reais).
            if (numeros.length() > 6) {
                numeros = numeros.substring(numeros.length() - 6);
            }

            return numeros;

        } catch (Exception e) {
            System.err.println("Erro Tacito: " + e.getMessage());
            return "";
        } finally {
            tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_AUTO);
        }
    }

    private BufferedImage limparRuidoComErosao(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage dest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = dest.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        g.dispose();

        for (int y = 0; y < height; y++) {
            for (int x = 1; x < width - 1; x++) {
                boolean pixelAtualEhEscuro = isEscuro(src.getRGB(x, y));
                boolean vizinhoEsqEhEscuro = isEscuro(src.getRGB(x - 1, y));
                boolean vizinhoDirEhEscuro = isEscuro(src.getRGB(x + 1, y));

                // Erosão: só mantém o preto se tiver vizinhos pretos (elimina riscos finos)
                if (pixelAtualEhEscuro && vizinhoEsqEhEscuro && vizinhoDirEhEscuro) {
                    dest.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    dest.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }
        return dest;
    }

    private boolean isEscuro(int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb & 0xFF);
        return ((r + g + b) / 3.0) < 100;
    }
}