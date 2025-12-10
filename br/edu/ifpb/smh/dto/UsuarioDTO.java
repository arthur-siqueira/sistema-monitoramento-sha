package br.edu.ifpb.smh.dto;

public class UsuarioDTO {
    private String id;
    private String nome;

    public UsuarioDTO(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    public String getId() { return id; }
    public String getNome() { return nome; }
}