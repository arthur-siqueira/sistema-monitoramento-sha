package br.edu.ifpb.smh.dto;

public class HistoricoConsumoDTO {
    private String shaId;
    private double valorTotal;

    public HistoricoConsumoDTO(String shaId, double valorTotal) {
        this.shaId = shaId;
        this.valorTotal = valorTotal;
    }
    public double getValorTotal() { return valorTotal; }
}