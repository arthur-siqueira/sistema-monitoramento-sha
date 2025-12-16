package br.edu.ifpb.smh.services;

import br.edu.ifpb.smh.dto.*;
import java.util.*;

public class AlertaService {

    // Banco de dados de regras: UsuarioID -> Lista de Regras
    private final Map<String, List<RegraAlertaDTO>> regrasDB = new HashMap<>();

    // Lista de alertas ativos para exibição no dashboard
    private final List<AlertaAtivoDTO> alertasAtivos = new ArrayList<>();

    // Dependência (O AlertaService precisa notificar alguém)
    // Em um cenário ideal, isso seria injetado, mas vamos instanciar ou setar depois
    private NotificacaoService notificacaoService = new NotificacaoService();

    public RegraAlertaDTO configurarRegra(ConfigAlertaRequest request) {
        RegraAlertaDTO novaRegra = new RegraAlertaDTO(
                UUID.randomUUID().toString(),
                request.getUsuarioId(),
                request.getLimiteLitros(),
                true
        );

        regrasDB.putIfAbsent(request.getUsuarioId(), new ArrayList<>());
        regrasDB.get(request.getUsuarioId()).add(novaRegra);

        System.out.println("[AlertaService] Regra configurada para User " + request.getUsuarioId() + ": Limite " + request.getLimiteLitros());
        return novaRegra;
    }

    public List<AlertaAtivoDTO> getAlertasAtivos() {
        return alertasAtivos;
    }

    /**
     * Método CRÍTICO: Chamado sempre que chega uma nova leitura do sensor.
     */
    public void verificarRegras(String usuarioId, double consumoAtual) {
        List<RegraAlertaDTO> regras = regrasDB.get(usuarioId);
        if (regras == null) return;

        for (RegraAlertaDTO regra : regras) {
            if (regra.isAtiva() && consumoAtual >= regra.getLimiteConfigurado()) {

                String msg = String.format("ATENÇÃO: Consumo de %.2f m3 excedeu o limite de %.2f m3!", consumoAtual, regra.getLimiteConfigurado());

                // 1. Registra como alerta ativo no painel
                alertasAtivos.add(new AlertaAtivoDTO(usuarioId, msg, consumoAtual, regra.getLimiteConfigurado()));

                // 2. Dispara notificação externa
                // (Aqui simplificamos assumindo canais padrão, mas deveria vir da config da regra)
                List<String> canais = Arrays.asList("EMAIL");
                Alerta alertaObjeto = new Alerta(msg, "cliente@email.com", canais);

                notificacaoService.enviarNotificacao(alertaObjeto);
            }
        }
    }
}