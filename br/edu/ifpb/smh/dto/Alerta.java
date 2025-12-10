package br.edu.ifpb.smh.dto;

public class Alerta {
    private String mensagem;
    private String tipoCanal; // "EMAIL" ou "WEBHOOK"

    public Alerta(String mensagem, String tipoCanal) {
        this.mensagem = mensagem;
        this.tipoCanal = tipoCanal;
    }
    public String getMensagem() { return mensagem; }
    public String getTipoCanal() { return tipoCanal; }
}