package br.edu.ifpb.smh.hardware;

import br.edu.ifpb.smh.services.SensorService;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

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


        File[] imagens = pasta.listFiles(f -> {
            String nome = f.getName().toLowerCase();
            return (nome.endsWith(".jpg") || nome.endsWith(".jpeg") || nome.endsWith(".png"))
                    && !nome.endsWith(".processed");
        });

        if (imagens == null || imagens.length == 0) return;


        Arrays.sort(imagens, Comparator.comparingLong(File::lastModified));

        for (File img : imagens) {
            try {
                System.out.println("[PASTA] Processando nova imagem de " + shaId + ": " + img.getName());

                // 1. Chama o serviço para fazer OCR
                service.processarImagemOCR(shaId, img);

                // 2. Renomeia para não processar de novo
                // O arquivo passará a se chamar "1.jpeg.processed"
                File processado = new File(img.getAbsolutePath() + ".processed");
                boolean renomeou = img.renameTo(processado);

                if (!renomeou) {
                    System.err.println("Aviso: Não foi possível renomear/marcar a imagem " + img.getName());
                }

            } catch (Exception e) {
                System.err.println("Erro ao ler pasta " + shaId + ": " + e.getMessage());
            }
        }
    }
}