package br.edu.ifpb.smh.dto;

import java.util.List;

public class ConfigAlertaRequest {
    private String usuarioId;
    private double limiteLitros;     // Ex: 70.0
    private int periodoHoras;        // Ex: 24
    private List<String> canaisStrategy; // Ex: ["EMAIL", "WEBHOOK"]
    private List<String> destinatarios;  // Ex: ["admin@cagepa.com"]

    public ConfigAlertaRequest(String usuarioId, double limiteLitros, int periodoHoras, List<String> canais, List<String> dest) {
        this.usuarioId = usuarioId;
        this.limiteLitros = limiteLitros;
        this.periodoHoras = periodoHoras;
        this.canaisStrategy = canais;
        this.destinatarios = dest;
    }

    public String getUsuarioId() { return usuarioId; }
    public double getLimiteLitros() { return limiteLitros; }
    public List<String> getCanaisStrategy() { return canaisStrategy; }
    public List<String> getDestinatarios() { return destinatarios; }
}