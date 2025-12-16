package br.edu.ifpb.smh.facade;

import br.edu.ifpb.smh.dto.*;
import java.util.Date;
import java.util.List;

public interface ISMH_Facade {

    // =========================================================================
    // 1. GESTÃO DE USUÁRIOS E CONTAS (RF-001)
    // =========================================================================

    /**
     * Cria um novo usuário (cliente) no sistema.
     * DELEGA PARA: SubsistemaDeUsuarios
     */
    UsuarioDTO criarUsuario(NovoUsuarioRequest request); // [cite: 17]

    /**
     * Busca os dados de um usuário pelo seu ID.
     */
    UsuarioDTO getUsuario(String usuarioId); // [cite: 21]

    /**
     * Atualiza dados cadastrais de um usuário.
     */
    UsuarioDTO atualizarUsuario(String usuarioId, AtualizaUsuarioRequest request); // [cite: 26]

    /**
     * Associa um novo hidrômetro (SHA) a um usuário existente.
     */
    SHA_DTO vincularShaAoUsuario(String usuarioId, String shaId, String endereco); // [cite: 33]

    /**
     * Lista todos os SHAs vinculados a um usuário.
     */
    List<SHA_DTO> listarShasDoUsuario(String usuarioId); // [cite: 38]


    // =========================================================================
    // 2. MONITORAMENTO DE CONSUMO (RF-002)
    // =========================================================================

    /**
     * Retorna o histórico de consumo de um único hidrômetro (SHA).
     * DELEGA PARA: SubsistemaDeSensores
     */
    HistoricoConsumoDTO getConsumoPorSha(String shaId, Date inicio, Date fim); // [cite: 48]

    /**
     * Retorna o consumo AGREGADO (soma) de TODOS os SHAs de um usuário.
     * ORQUESTRAÇÃO: UsuarioService + SensorService
     */
    HistoricoConsumoDTO getConsumoAgregadoPorUsuario(String usuarioId, Date inicio, Date fim); // [cite: 58]

    /**
     * Retorna a leitura mais recente (tempo real) de um usuário.
     */
    ConsumoAtualDTO getConsumoAtualUsuario(String usuarioId); // [cite: 65]


    // =========================================================================
    // 3. SISTEMA DE ALERTAS (RF-003)
    // =========================================================================

    /**
     * Cria ou atualiza uma regra de alerta para um usuário.
     * DELEGA PARA: SubsistemaDeAlertas
     */
    RegraAlertaDTO configurarRegraAlerta(ConfigAlertaRequest request); // [cite: 77]

    /**
     * Retorna os alertas ativos no momento.
     */
    List<AlertaAtivoDTO> getAlertasAtivos(); // [cite: 82]

    /**
     * Método para disparar a notificação (Strategy).
     */
    void dispararNotificacao(Alerta alerta); // [cite: 88]
}