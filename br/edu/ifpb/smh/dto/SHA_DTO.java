package br.edu.ifpb.smh.dto;

public class SHA_DTO {
    private String id;        // Ex: "SHA-TAC-02"
    private String usuarioId; // Dono do SHA
    private String enderecoInstalacao;

    public SHA_DTO(String id, String usuarioId, String enderecoInstalacao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.enderecoInstalacao = enderecoInstalacao;
    }

    public String getId() { return id; }
    public String getUsuarioId() { return usuarioId; }
    public String getEnderecoInstalacao() { return enderecoInstalacao; }
}