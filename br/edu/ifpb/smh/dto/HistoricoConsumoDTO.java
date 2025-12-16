package br.edu.ifpb.smh.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class HistoricoConsumoDTO {
    private String referenciaId; // Pode ser ID do SHA ou do Usuário
    private Date periodoInicio;
    private Date periodoFim;
    private double consumoTotalMetrosCubicos;
    // Mapa simples: Data -> Valor Lido
    private Map<Date, Double> leiturasDiarias;

    public HistoricoConsumoDTO(String referenciaId, Date inicio, Date fim, double total, Map<Date, Double> leituras) {
        this.referenciaId = referenciaId;
        this.periodoInicio = inicio;
        this.periodoFim = fim;
        this.consumoTotalMetrosCubicos = total;
        this.leiturasDiarias = leituras;
    }

    public String getReferenciaId() { return referenciaId; }
    public double getConsumoTotalMetrosCubicos() { return consumoTotalMetrosCubicos; }
    public Map<Date, Double> getLeiturasDiarias() { return leiturasDiarias; }
}