package br.edu.ifpb.smh.strategy;

public class WebhookStrategy implements CanalNotificacaoStrategy {
    @Override
    public void enviar(String mensagem, String destino) {
        System.out.println("[WEBHOOK Strategy] POST " + destino + " payload={" + mensagem + "}");
    }
}