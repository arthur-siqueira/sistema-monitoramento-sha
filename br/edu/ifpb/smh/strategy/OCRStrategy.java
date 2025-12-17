package br.edu.ifpb.smh.strategy;

import net.sourceforge.tess4j.Tesseract;
import java.awt.image.BufferedImage;

public interface OCRStrategy {
    /**
     * Recebe a imagem crua, trata (recorta, zoom) e devolve o texto lido.
     */
    String processar(Tesseract tesseract, BufferedImage imagemOriginal);
}