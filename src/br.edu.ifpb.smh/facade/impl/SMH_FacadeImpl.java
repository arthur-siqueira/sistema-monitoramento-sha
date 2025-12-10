package br.edu.ifpb.smh.facade.impl;

import br.edu.ifpb.smh.facade.ISMH_Facade;
import br.edu.ifpb.smh.services.*;
import br.edu.ifpb.smh.dto.*;
import java.util.Date;

public class SMH_FacadeImpl implements ISMH_Facade {
    private UsuarioService usuarioService;
    private SensorService sensorService;
    private NotificacaoService notificacaoService;

    // Construtor que recebe o SensorService (Injeção de Dependência para teste)
    public SMH_FacadeImpl(SensorService sensorService) {
        this.sensorService = sensorService;
        this.usuarioService = new UsuarioService();
        this.notificacaoService = new NotificacaoService();
    }

    @Override
    public UsuarioDTO criarUsuario(NovoUsuarioRequest request) {
        return usuarioService.criarUsuario(request);
    }

    @Override
    public SHA_DTO vincularShaAoUsuario(String usuarioId, String shaId, String endereco) {
        System.out.println("[Facade] Vinculando SHA " + shaId + " ao usuario " + usuarioId);
        return new SHA_DTO(shaId, endereco);
    }

    @Override
    public HistoricoConsumoDTO getConsumoPorSha(String shaId, Date inicio, Date fim) {
        return sensorService.getLeitura(shaId);
    }

    @Override
    public void dispararNotificacao(Alerta alerta) {
        notificacaoService.notificar(alerta.getMensagem(), "admin@ifpb.edu.br", alerta.getTipoCanal());
    }
}