package br.edu.ifpb.smh.dto;

public class UsuarioDTO {
    private String id;
    private String nome;
    private String email;
    private String endereco;

    public UsuarioDTO(String id, String nome, String email, String endereco) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
    }

    // Getters
    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getEndereco() { return endereco; }

    @Override
    public String toString() {
        return "UsuarioDTO{id='" + id + "', nome='" + nome + "'}";
    }
}