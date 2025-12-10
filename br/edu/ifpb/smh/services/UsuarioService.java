package br.edu.ifpb.smh.services;

import br.edu.ifpb.smh.dto.NovoUsuarioRequest;
import br.edu.ifpb.smh.dto.UsuarioDTO;
import java.util.UUID;

public class UsuarioService {
    public UsuarioDTO criarUsuario(NovoUsuarioRequest req) {
        return new UsuarioDTO(UUID.randomUUID().toString(), req.getNome());
    }
}