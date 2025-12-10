package br.edu.ifpb.smh.dto;

public class NovoUsuarioRequest {
    private String nome;
    private String cpf;

    public NovoUsuarioRequest(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }
    public String getNome() { return nome; }
}