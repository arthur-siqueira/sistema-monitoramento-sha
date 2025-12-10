package br.edu.ifpb.smh;

import br.edu.ifpb.smh.dto.*;
import br.edu.ifpb.smh.facade.ISMH_Facade;
import br.edu.ifpb.smh.facade.impl.SMH_FacadeImpl;
import br.edu.ifpb.smh.hardware.SimuladorSHA;
import br.edu.ifpb.smh.services.SensorService;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 🚰 PAINEL DE MONITORAMENTO SMH (IFPB) ===");

        // 1. Setup da Infraestrutura
        SensorService sensorCompartilhado = new SensorService();
        ISMH_Facade painel = new SMH_FacadeImpl(sensorCompartilhado);

        // 2. Configuração (Facade em ação)
        UsuarioDTO u1 = painel.criarUsuario(new NovoUsuarioRequest("Renato", "123"));
        UsuarioDTO u2 = painel.criarUsuario(new NovoUsuarioRequest("Maria", "456"));

        // 3. Hardware Físico (Simuladores)
        SimuladorSHA hardware1 = new SimuladorSHA("SHA-RENATO-01", 1000.0);
        SimuladorSHA hardware2 = new SimuladorSHA("SHA-MARIA-02", 500.0);

        painel.vincularShaAoUsuario(u1.getId(), hardware1.getId(), "Cozinha");
        painel.vincularShaAoUsuario(u2.getId(), hardware2.getId(), "Jardim");

        // 4. Execução (Loop de 3 ciclos)
        for (int i = 1; i <= 3; i++) {
            System.out.println("\n--- CICLO DE LEITURA " + i + " ---");

            // A: Sensores enviam dados
            sensorCompartilhado.registrarLeitura(hardware1.getId(), hardware1.realizarLeitura());
            sensorCompartilhado.registrarLeitura(hardware2.getId(), hardware2.realizarLeitura());

            // B: Painel consulta dados
            System.out.println(">> App do Renato consulta: "
                    + String.format("%.2f", painel.getConsumoPorSha(hardware1.getId(), new Date(), new Date()).getValorTotal()) + " L");

            System.out.println(">> App da Maria consulta:  "
                    + String.format("%.2f", painel.getConsumoPorSha(hardware2.getId(), new Date(), new Date()).getValorTotal()) + " L");

            Thread.sleep(1000);
        }

        // 5. Teste do Strategy
        System.out.println("\n--- TESTE DE NOTIFICAÇÃO (STRATEGY) ---");
        painel.dispararNotificacao(new Alerta("Vazamento Detectado!", "EMAIL"));
        painel.dispararNotificacao(new Alerta("Erro de Conexão!", "WEBHOOK"));
    }
}