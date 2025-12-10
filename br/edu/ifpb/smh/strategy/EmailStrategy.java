package br.edu.ifpb.smh.strategy;

public class EmailStrategy implements CanalNotificacaoStrategy {
    @Override
    public void enviar(String mensagem, String destino) {
        System.out.println("[EMAIL Strategy] Enviando para " + destino + ": " + mensagem);
    }
}