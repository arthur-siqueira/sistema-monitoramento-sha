package br.edu.ifpb.smh.facade;

import br.edu.ifpb.smh.dto.*;
import br.edu.ifpb.smh.services.SensorService;
import br.edu.ifpb.smh.services.UsuarioService;
import br.edu.ifpb.smh.services.AlertaService;
import br.edu.ifpb.smh.services.NotificacaoService;

import java.util.Date;
import java.util.List;

public class MonitoramentoFacade implements ISMH_Facade {

    // Subsistemas instanciados (Composição)
    private final SensorService sensorService;
    private final UsuarioService usuarioService;
    private final AlertaService alertaService;
    private final NotificacaoService notificacaoService;

    public MonitoramentoFacade() {
        // Inicializa os subsistemas
        this.alertaService = new AlertaService();
        this.notificacaoService = new NotificacaoService();
        this.usuarioService = new UsuarioService();

        // Injeta o AlertaService dentro do SensorService
        // Isso permite que o Sensor avise quando chegar dado novo
        this.sensorService = new SensorService(this.alertaService);
    }

    // Método extra para iniciar o monitoramento em background (OCR)
    // Isso inicia a thread que fica varrendo as pastas/arquivos
    public void iniciarMonitoramento() {
        System.out.println(">>> Iniciando Sistema de Monitoramento SMH...");
        sensorService.iniciarVarredura();
    }

    // ==========================================================
    // IMPLEMENTAÇÃO DOS MÉTODOS DA INTERFACE ISMH_Facade
    // ==========================================================

    @Override
    public UsuarioDTO criarUsuario(NovoUsuarioRequest request) {
        // Delega para o Subsistema de Usuários
        return usuarioService.criarUsuario(request);
    }

    @Override
    public UsuarioDTO getUsuario(String usuarioId) {
        return usuarioService.getUsuario(usuarioId);
    }

    @Override
    public UsuarioDTO atualizarUsuario(String usuarioId, AtualizaUsuarioRequest request) {
        return usuarioService.atualizarUsuario(usuarioId, request);
    }

    @Override
    public SHA_DTO vincularShaAoUsuario(String usuarioId, String shaId, String endereco) {
        return usuarioService.vincularSha(usuarioId, shaId, endereco);
    }

    @Override
    public List<SHA_DTO> listarShasDoUsuario(String usuarioId) {
        return usuarioService.listarShas(usuarioId);
    }

    @Override
    public HistoricoConsumoDTO getConsumoPorSha(String shaId, Date inicio, Date fim) {
        // Delega para o Subsistema de Sensores
        return sensorService.getConsumoPorSha(shaId, inicio, fim);
    }

    @Override
    public HistoricoConsumoDTO getConsumoAgregadoPorUsuario(String usuarioId, Date inicio, Date fim) {
        // Orquestração: Pega SHAs do usuário e busca consumo de cada um
        List<SHA_DTO> shas = usuarioService.listarShas(usuarioId);
        return sensorService.getConsumoAgregado(shas, inicio, fim);
    }

    @Override
    public ConsumoAtualDTO getConsumoAtualUsuario(String usuarioId) {
        // Lógica para pegar leitura mais recente (tempo real)
        List<SHA_DTO> shas = usuarioService.listarShas(usuarioId);
        // Simplificação: soma o atual de todos
        return sensorService.getConsumoAtualAgregado(shas);
    }

    @Override
    public RegraAlertaDTO configurarRegraAlerta(ConfigAlertaRequest request) {
        return alertaService.configurarRegra(request);
    }

    @Override
    public List<AlertaAtivoDTO> getAlertasAtivos() {
        return alertaService.getAlertasAtivos();
    }

    @Override
    public void dispararNotificacao(Alerta alerta) {
        notificacaoService.enviarNotificacao(alerta);
    }
}