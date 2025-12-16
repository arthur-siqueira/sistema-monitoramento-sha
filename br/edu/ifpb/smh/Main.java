package br.edu.ifpb.smh;

import br.edu.ifpb.smh.dto.*;
import br.edu.ifpb.smh.facade.MonitoramentoFacade;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   SISTEMA DE MONITORAMENTO DE HIDRÔMETROS (SMH)  ");
        System.out.println("   Iniciando via Console...");
        System.out.println("==================================================");

        // 1. Instancia a Fachada (O único ponto de contato)
        MonitoramentoFacade facade = new MonitoramentoFacade();

        // 2. Inicia o motor de monitoramento (Threads de OCR)
        facade.iniciarMonitoramento();

        try {
            // -----------------------------------------------------------
            // CENÁRIO 1: CADASTRO (RF-001)
            // -----------------------------------------------------------
            System.out.println("\n>>> [PASSO 1] Criando Usuário e Vinculando SHAs...");

            NovoUsuarioRequest novoUser = new NovoUsuarioRequest(
                    "Arthur Siqueira", "123.456.789-00", "arthur@email.com", "Rua do Projeto, 100"
            );
            UsuarioDTO usuario = facade.criarUsuario(novoUser);

            // VINCULANDO OS SENSORES (IDs devem bater com o SensorService)
            // Vincula o simulador de arquivo único
            facade.vincularShaAoUsuario(usuario.getId(), "SHA-ARTHUR-01", "Jardim");
            // Vincula o simulador de pasta
            //facade.vincularShaAoUsuario(usuario.getId(), "SHA-TAC-01", "Cozinha");

            // -----------------------------------------------------------
            // CENÁRIO 2: CONFIGURAÇÃO DE REGRAS (RF-003)
            // -----------------------------------------------------------
            System.out.println("\n>>> [PASSO 2] Configurando Regras de Alerta...");

            // Define limite baixo (0.010 m3) para testar o disparo fácil
            List<String> canais = Arrays.asList("EMAIL", "WEBHOOK");
            ConfigAlertaRequest config = new ConfigAlertaRequest(
                    usuario.getId(), 0.010, 24, canais, Arrays.asList("admin@cagepa.com.br")
            );

            facade.configurarRegraAlerta(config);

            // -----------------------------------------------------------
            // CENÁRIO 3: MONITORAMENTO EM TEMPO REAL
            // -----------------------------------------------------------
            System.out.println("\n>>> [SISTEMA RODANDO] Aguardando leituras dos simuladores...");
            System.out.println("    (Abra seu simulador e gere imagens agora!)");
            System.out.println("    Digite 'sair' para encerrar ou 'status' para ver alertas.");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                String comando = scanner.nextLine();

                if (comando.equalsIgnoreCase("sair")) {
                    System.out.println("Encerrando sistema...");
                    System.exit(0);
                }
                else if (comando.equalsIgnoreCase("status")) {
                    List<AlertaAtivoDTO> alertas = facade.getAlertasAtivos();
                    System.out.println("\n--- STATUS ATUAL ---");
                    if(alertas.isEmpty()) System.out.println("Nenhum alerta ativo. Consumo normal.");
                    alertas.forEach(System.out::println);
                    System.out.println("--------------------\n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}