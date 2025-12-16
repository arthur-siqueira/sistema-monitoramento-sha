package br.edu.ifpb.smh.hardware;

import br.edu.ifpb.smh.services.SensorService;
import java.io.File;

public class AdapterSimuladorPasta implements LeitorSHA {
    private final String caminhoPasta;
    private final String shaId;

    public AdapterSimuladorPasta(String shaId, String caminhoPasta) {
        this.shaId = shaId;
        this.caminhoPasta = caminhoPasta;
    }

    @Override
    public void processar(SensorService service) {
        File pasta = new File(caminhoPasta);
        if (!pasta.exists()) return;

        // Filtra imagens que NÃO terminam com .processed
        File[] imagens = pasta.listFiles(f ->
                (f.getName().toLowerCase().endsWith(".jpg") ||
                        f.getName().toLowerCase().endsWith(".png")) &&
                        !f.getName().endsWith(".processed")
        );

        if (imagens == null) return;

        for (File img : imagens) {
            try {
                System.out.println("[PASTA] Processando nova imagem de " + shaId + ": " + img.getName());

                // 1. Chama o serviço para fazer OCR e salvar
                service.processarImagemOCR(shaId, img);

                // 2. Renomeia para não processar de novo
                File processado = new File(img.getAbsolutePath() + ".processed");
                img.renameTo(processado);

            } catch (Exception e) {
                System.err.println("Erro ao ler pasta " + shaId + ": " + e.getMessage());
            }
        }
    }
}