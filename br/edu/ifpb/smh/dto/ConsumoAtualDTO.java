package br.edu.ifpb.smh.dto;

import java.util.Date;

public class ConsumoAtualDTO {
    private String usuarioId;
    private double leituraAtual; // Em m3
    private Date dataUltimaLeitura;
    private boolean alertaAtivo; // Se passou de 70L, por exemplo

    public ConsumoAtualDTO(String usuarioId, double leituraAtual, Date dataUltimaLeitura, boolean alertaAtivo) {
        this.usuarioId = usuarioId;
        this.leituraAtual = leituraAtual;
        this.dataUltimaLeitura = dataUltimaLeitura;
        this.alertaAtivo = alertaAtivo;
    }

    public double getLeituraAtual() { return leituraAtual; }
    public boolean isAlertaAtivo() { return alertaAtivo; }

    @Override
    public String toString() {
        return String.format("Consumo: %.3f m3 (Alerta: %s)", leituraAtual, alertaAtivo);
    }
}