package br.edu.ifpb.smh.strategy;

import net.sourceforge.tess4j.Tesseract;
import java.awt.*;
import java.awt.image.BufferedImage;

public class OCRArthurStrategy implements OCRStrategy {

    @Override
    public String processar(Tesseract tesseract, BufferedImage original) {
        try {
            // Lógica Específica do Simulador do Arthur:
            // 1. Recorte para ignorar cabeçalho e rodapé
            int y = (int) (original.getHeight() * 0.25);
            int h = (int) (original.getHeight() * 0.35);
            int w = original.getWidth();
            BufferedImage recortada = original.getSubimage(0, y, w, h);

            // 2. Zoom 2x para melhorar leitura
            BufferedImage finalImg = escalarImagem(recortada, 2.0);

            // 3. OCR
            return tesseract.doOCR(finalImg);

        } catch (Exception e) {
            System.err.println("Erro na estratégia Arthur: " + e.getMessage());
            return "";
        }
    }

    // Método auxiliar de zoom
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
}