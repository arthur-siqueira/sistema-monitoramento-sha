package br.edu.ifpb.smh;

import br.edu.ifpb.smh.dto.*;
import br.edu.ifpb.smh.facade.MonitoramentoFacade;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   SISTEMA DE MONITORAMENTO DE HIDRÔMETROS (SMH)  ");
        System.out.println("   Versão Final - Smart Home Integration          ");
        System.out.println("==================================================");

        MonitoramentoFacade facade = new MonitoramentoFacade();
        facade.iniciarMonitoramento();

        try {
            // CENÁRIO: UM DONO, MÚLTIPLOS SENSORES (SMART HOME)
            System.out.println("\n>>> [PASSO 1] Criando Usuário Principal...");

            NovoUsuarioRequest novoUser = new NovoUsuarioRequest(
                    "Arthur Siqueira", "123.456.789-00", "arthur@email.com", "Residência Principal"
            );
            UsuarioDTO usuario = facade.criarUsuario(novoUser);
            System.out.println("    Usuário ID: " + usuario.getId());

            // --- VINCULANDO TUDO AO MESMO USUÁRIO ---

            // 1. Arthur (Arquivo)
            facade.vincularShaAoUsuario(usuario.getId(), "SHA-ARTHUR-01", "Apartamento (Sensor Arthur)");

            // 2. Cefras (Pasta)
            facade.vincularShaAoUsuario(usuario.getId(), "SHA-CEFRAS-01", "Casa (Sensor Cefras)");

            // 3. Tácito (Pasta)
            facade.vincularShaAoUsuario(usuario.getId(), "SHA-TAC-01", "Fazenda (Sensor Tácito)");

            // --- REGRAS ---
            System.out.println("\n>>> [PASSO 3] Configurando Alerta de Consumo Agregado...");

            facade.configurarRegraAlerta(new ConfigAlertaRequest(
                    usuario.getId(), 0.010, 24, Arrays.asList("EMAIL"), Arrays.asList("admin@cagepa.com.br")
            ));
            System.out.println("    Monitoramento Ativo. Limite: 0.010 m³");

            // --- LOOP ---
            System.out.println("\n>>> [SISTEMA RODANDO] Aguardando leituras...");
            System.out.println("    (Gere imagens nos 3 simuladores agora!)");
            System.out.println("    Digite 'status' para ver alertas ou 'sair' para encerrar.\n");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                if (scanner.hasNext()) {
                    String comando = scanner.nextLine();
                    if (comando.equalsIgnoreCase("sair")) System.exit(0);
                    else if (comando.equalsIgnoreCase("status")) {
                        var alertas = facade.getAlertasAtivos();
                        if(alertas.isEmpty()) System.out.println("--- Status: Consumo Normal ---");
                        else {
                            System.out.println("\n--- ALERTAS ATIVOS ---");
                            alertas.forEach(System.out::println);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}