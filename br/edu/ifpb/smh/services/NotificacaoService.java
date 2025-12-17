package br.edu.ifpb.smh.services;

import br.edu.ifpb.smh.dto.Alerta;
import br.edu.ifpb.smh.strategy.*;
import java.util.HashMap;
import java.util.Map;

public class NotificacaoService {

    private final Map<String, CanalNotificacaoStrategy> estrategias = new HashMap<>();

    public NotificacaoService() {
        // Registra as estratégias disponíveis no sistema
        estrategias.put("EMAIL", new EmailStrategy());
        estrategias.put("WEBHOOK", new WebhookStrategy());
    }

    public void enviarNotificacao(Alerta alerta) {
        // Para cada canal configurado no alerta (ex: ["EMAIL", "WEBHOOK"])
        for (String canal : alerta.getCanaisEnvio()) {
            CanalNotificacaoStrategy estrategia = estrategias.get(canal.toUpperCase());

            if (estrategia != null) {
                estrategia.enviar(alerta);
            } else {
                System.out.println("ERRO: Canal de notificação não suportado: " + canal);
            }
        }
    }
}