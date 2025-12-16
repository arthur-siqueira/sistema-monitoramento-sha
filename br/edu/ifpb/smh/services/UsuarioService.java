package br.edu.ifpb.smh.services;

import br.edu.ifpb.smh.dto.*;

import java.util.*;

public class UsuarioService {

    // Simulação de Banco de Dados em Memória
    // Tabela: ID -> UsuarioDTO
    private final Map<String, UsuarioDTO> usuariosDB = new HashMap<>();

    // Tabela de Relacionamento: ID_Usuario -> Lista de SHAs
    private final Map<String, List<SHA_DTO>> shasPorUsuarioDB = new HashMap<>();

    public UsuarioService() {
        // Carga inicial de dados para testes (opcional)
        // Isso ajuda a não ter que criar usuário toda vez que rodar
        String idTeste = "123";
        usuariosDB.put(idTeste, new UsuarioDTO(idTeste, "Cliente Teste", "teste@email.com", "Rua A"));
        shasPorUsuarioDB.put(idTeste, new ArrayList<>());
    }

    // --- CRUD DE USUÁRIOS [cite: 10] ---

    public UsuarioDTO criarUsuario(NovoUsuarioRequest request) {
        // Gera um ID único (simulando auto-increment ou UUID)
        String novoId = UUID.randomUUID().toString().substring(0, 8);

        UsuarioDTO novoUsuario = new UsuarioDTO(
                novoId,
                request.getNome(),
                request.getEmail(),
                request.getEndereco()
        );

        // Salva no "banco"
        usuariosDB.put(novoId, novoUsuario);
        shasPorUsuarioDB.put(novoId, new ArrayList<>()); // Inicializa lista de SHAs vazia

        System.out.println("[UsuarioService] Usuário criado: " + novoUsuario.getNome() + " (ID: " + novoId + ")");
        return novoUsuario;
    }

    public UsuarioDTO getUsuario(String usuarioId) {
        return usuariosDB.get(usuarioId);
    }

    public UsuarioDTO atualizarUsuario(String usuarioId, AtualizaUsuarioRequest request) {
        UsuarioDTO existente = usuariosDB.get(usuarioId);
        if (existente == null) return null;

        // Atualiza apenas os campos permitidos
        // Nota: Em um sistema real, faríamos getters/setters ou um novo objeto
        UsuarioDTO atualizado = new UsuarioDTO(
                existente.getId(),
                existente.getNome(), // Nome não muda
                request.getEmail() != null ? request.getEmail() : existente.getEmail(),
                request.getEndereco() != null ? request.getEndereco() : existente.getEndereco()
        );

        usuariosDB.put(usuarioId, atualizado);
        System.out.println("[UsuarioService] Usuário atualizado: " + usuarioId);
        return atualizado;
    }

    // --- GESTÃO DE HIDRÔMETROS (Vinculação)  ---

    public SHA_DTO vincularSha(String usuarioId, String shaId, String endereco) {
        if (!usuariosDB.containsKey(usuarioId)) {
            throw new RuntimeException("Usuário não encontrado: " + usuarioId);
        }

        SHA_DTO novoSha = new SHA_DTO(shaId, usuarioId, endereco);

        // Adiciona à lista do usuário
        shasPorUsuarioDB.get(usuarioId).add(novoSha);

        System.out.println("[UsuarioService] SHA " + shaId + " vinculado ao usuário " + usuarioId);
        return novoSha;
    }

    public List<SHA_DTO> listarShas(String usuarioId) {
        return shasPorUsuarioDB.getOrDefault(usuarioId, Collections.emptyList());
    }
}