package br.edu.ifpb.smh.hardware;

import br.edu.ifpb.smh.services.SensorService;
import java.io.File;

public class AdapterSimuladorUnico implements LeitorSHA {
    private final String caminhoArquivo;
    private final String shaId;
    private long ultimaModificacao = 0;

    public AdapterSimuladorUnico(String shaId, String caminhoArquivo) {
        this.shaId = shaId;
        this.caminhoArquivo = caminhoArquivo;
    }

    @Override
    public void processar(SensorService service) {
        File arquivo = new File(caminhoArquivo);

        // Verifica se arquivo existe e se foi modificado desde a última leitura
        if (arquivo.exists() && arquivo.lastModified() > ultimaModificacao) {
            // Atualiza o timestamp IMEDIATAMENTE para evitar loop se o OCR for rápido
            this.ultimaModificacao = arquivo.lastModified();

            try {
                System.out.println("[UNICO] Arquivo atualizado detectado: " + shaId);

                // Chama o OCR (NÃO renomeia o arquivo, pois o simulador precisa dele lá)
                service.processarImagemOCR(shaId, arquivo);

            } catch (Exception e) {
                System.err.println("Erro ao ler arquivo único " + shaId + ": " + e.getMessage());
            }
        }
    }
}