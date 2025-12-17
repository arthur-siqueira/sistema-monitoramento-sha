package br.edu.ifpb.smh.strategy;

import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class OCRCefrasStrategy implements OCRStrategy {

    @Override
    public String processar(Tesseract tesseract, BufferedImage original) {
        try {
            // PASSO 1: CONFIGURAÇÃO DE MODO
            tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_SINGLE_LINE);
            // Whitelist apenas números
            tesseract.setTessVariable("tessedit_char_whitelist", "0123456789");

            // PASSO 2: RECORTE (CROP) AJUSTADO
            int x = (int) (original.getWidth() * 0.30);
            int y = (int) (original.getHeight() * 0.38);
            int w = (int) (original.getWidth() * 0.32);
            // CORREÇÃO AQUI: Reduzi de 0.18 para 0.12 para cortar o texto "m3/h" de baixo
            int h = (int) (original.getHeight() * 0.12);

            // Validação de bordas
            if (x + w > original.getWidth()) w = original.getWidth() - x;
            if (y + h > original.getHeight()) h = original.getHeight() - y;

            BufferedImage recortada = original.getSubimage(x, y, w, h);

            // PASSO 3: ZOOM
            // Aumentamos 5x para melhor definição
            BufferedImage ampliada = ImageHelper.getScaledInstance(recortada, recortada.getWidth() * 5, recortada.getHeight() * 5);

            // PASSO 4: LIMPEZA DE RUÍDO
            BufferedImage limpa = limparRuido(ampliada);

            // Salva debug atualizado
            ImageIO.write(limpa, "png", new File("debug_visao_cefras.png"));

            // PASSO 5: LEITURA
            String resultado = tesseract.doOCR(limpa);
            String numeros = resultado.replaceAll("[^0-9]", "").trim();

            // Correção de segurança para "zeros sujos"
            if (numeros.length() > 3 && numeros.startsWith("8")) {
                numeros = numeros.replaceFirst("^8+", "0");
            }

            return numeros;

        } catch (Exception e) {
            System.err.println("Erro na estratégia Cefras: " + e.getMessage());
            return "";
        } finally {
            tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_AUTO);
        }
    }

    private BufferedImage limparRuido(BufferedImage src) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < src.getWidth(); x++) {
            for (int y = 0; y < src.getHeight(); y++) {
                int rgb = src.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);
                double brilho = (r + g + b) / 3.0;

                // AJUSTE FINO NO THRESHOLD:
                // Aumentei levemente de 90 para 110.
                // Isso mantém mais pixels "cinza escuro" como PRETO,
                // ajudando a deixar os números menos "magros" ou falhados.
                if (brilho > 110) {
                    dest.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    dest.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return dest;
    }
}