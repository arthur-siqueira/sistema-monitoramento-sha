package br.edu.ifpb.smh.strategy;

import br.edu.ifpb.smh.dto.Alerta;

public class EmailStrategy implements CanalNotificacaoStrategy {
    @Override
    public void enviar(Alerta alerta) {
        System.out.println("==========================================");
        System.out.println("[E-MAIL SENDING] SMTP Server connecting...");
        System.out.println(" To: " + alerta.getDestinatarioPrincipal());
        System.out.println(" Subject: ALERTA DE CONSUMO CAGEPA");
        System.out.println(" Body: " + alerta.getMensagem());
        System.out.println("==========================================");
    }
}