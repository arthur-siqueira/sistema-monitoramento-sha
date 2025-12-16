package br.edu.ifpb.smh.hardware;

import br.edu.ifpb.smh.services.SensorService;

public interface LeitorSHA {
    /**
     * Método chamado periodicamente para verificar se há novas imagens.
     * @param service Referência ao serviço para chamar o OCR quando achar imagem.
     */
    void processar(SensorService service);
}