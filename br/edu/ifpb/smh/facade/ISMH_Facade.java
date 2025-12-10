package br.edu.ifpb.smh.facade;

import br.edu.ifpb.smh.dto.*;
import java.util.Date;

public interface ISMH_Facade {
    UsuarioDTO criarUsuario(NovoUsuarioRequest request);
    SHA_DTO vincularShaAoUsuario(String usuarioId, String shaId, String endereco);
    HistoricoConsumoDTO getConsumoPorSha(String shaId, Date inicio, Date fim);
    void dispararNotificacao(Alerta alerta);
}