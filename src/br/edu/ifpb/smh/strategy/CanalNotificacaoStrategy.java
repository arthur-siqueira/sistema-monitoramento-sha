package br.edu.ifpb.smh.strategy;

public interface CanalNotificacaoStrategy {
    void enviar(String mensagem, String destino);
}