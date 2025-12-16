package br.edu.ifpb.smh.dto;

public class NovoUsuarioRequest {
    private String nome;
    private String cpf;
    private String email;
    private String endereco;

    public NovoUsuarioRequest(String nome, String cpf, String email, String endereco) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.endereco = endereco;
    }

    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getEndereco() { return endereco; }
}