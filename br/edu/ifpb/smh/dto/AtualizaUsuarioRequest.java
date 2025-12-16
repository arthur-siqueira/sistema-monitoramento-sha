package br.edu.ifpb.smh.dto;

public class AtualizaUsuarioRequest {
    private String email;
    private String endereco;
    private String telefone;

    public AtualizaUsuarioRequest(String email, String endereco, String telefone) {
        this.email = email;
        this.endereco = endereco;
        this.telefone = telefone;
    }

    public String getEmail() { return email; }
    public String getEndereco() { return endereco; }
    public String getTelefone() { return telefone; }
}