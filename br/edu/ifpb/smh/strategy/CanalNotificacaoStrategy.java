package br.edu.ifpb.smh.strategy;

import br.edu.ifpb.smh.dto.Alerta;

public interface CanalNotificacaoStrategy {
    void enviar(Alerta alerta);
}