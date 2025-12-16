package br.edu.ifpb.smh.dto;

import java.util.Date;

public class AlertaAtivoDTO {
    private String usuarioId;
    private String mensagem;
    private double consumoAtual;
    private double limite;
    private Date dataDeteccao;

    public AlertaAtivoDTO(String usuarioId, String mensagem, double consumoAtual, double limite) {
        this.usuarioId = usuarioId;
        this.mensagem = mensagem;
        this.consumoAtual = consumoAtual;
        this.limite = limite;
        this.dataDeteccao = new Date();
    }

    @Override
    public String toString() {
        return "[ALERTA] Usuário " + usuarioId + ": " + mensagem;
    }
}