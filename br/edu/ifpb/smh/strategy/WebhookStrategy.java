package br.edu.ifpb.smh.strategy;

import br.edu.ifpb.smh.dto.Alerta;

public class WebhookStrategy implements CanalNotificacaoStrategy {
    @Override
    public void enviar(Alerta alerta) {
        System.out.println(">> [WEBHOOK] POST https://api.externa.com/v1/alertas payload={msg:'" + alerta.getMensagem() + "'}");
    }
}