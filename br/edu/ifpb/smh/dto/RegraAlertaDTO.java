package br.edu.ifpb.smh.dto;

public class RegraAlertaDTO {
    private String idRegra;
    private String usuarioId;
    private double limiteConfigurado;
    private boolean ativa; // Indica se a regra está valendo

    // Construtor
    public RegraAlertaDTO(String idRegra, String usuarioId, double limiteConfigurado, boolean ativa) {
        this.idRegra = idRegra;
        this.usuarioId = usuarioId;
        this.limiteConfigurado = limiteConfigurado;
        this.ativa = ativa;
    }

    // --- GETTERS ---

    public String getIdRegra() {
        return idRegra;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public double getLimiteConfigurado() {
        return limiteConfigurado;
    }

    public boolean isAtiva() {
        return ativa;
    }

    // ToString para facilitar seus testes e prints no console
    @Override
    public String toString() {
        return "RegraAlertaDTO{" +
                "id='" + idRegra + '\'' +
                ", usuario='" + usuarioId + '\'' +
                ", limite=" + limiteConfigurado +
                ", ativa=" + ativa +
                '}';
    }
}