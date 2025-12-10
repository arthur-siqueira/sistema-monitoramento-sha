package br.edu.ifpb.smh.services;

import br.edu.ifpb.smh.strategy.*;
import java.util.HashMap;
import java.util.Map;

public class NotificacaoService {
    private Map<String, CanalNotificacaoStrategy> estrategias = new HashMap<>();

    public NotificacaoService() {
        estrategias.put("EMAIL", new EmailStrategy());
        estrategias.put("WEBHOOK", new WebhookStrategy());
    }

    public void notificar(String mensagem, String destino, String canal) {
        CanalNotificacaoStrategy estrategia = estrategias.get(canal.toUpperCase());
        if (estrategia != null) {
            estrategia.enviar(mensagem, destino);
        } else {
            System.out.println("ERRO: Canal de notificação não suportado: " + canal);
        }
    }
}