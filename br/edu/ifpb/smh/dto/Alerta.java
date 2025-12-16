package br.edu.ifpb.smh.dto;

import java.util.List;

public class Alerta {
    private String mensagem;
    private String destinatarioPrincipal;
    private List<String> canaisEnvio; // "EMAIL", "SMS"

    public Alerta(String mensagem, String destinatarioPrincipal, List<String> canaisEnvio) {
        this.mensagem = mensagem;
        this.destinatarioPrincipal = destinatarioPrincipal;
        this.canaisEnvio = canaisEnvio;
    }

    public String getMensagem() { return mensagem; }
    public String getDestinatarioPrincipal() { return destinatarioPrincipal; }
    public List<String> getCanaisEnvio() { return canaisEnvio; }
}