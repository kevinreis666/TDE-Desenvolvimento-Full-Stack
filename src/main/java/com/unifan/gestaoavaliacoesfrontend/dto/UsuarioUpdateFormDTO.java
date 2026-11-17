package com.unifan.gestaoavaliacoesfrontend.dto;

public record UsuarioUpdateFormDTO(
    String email,
    String nome,
    String tipo,
    String senhaAntiga,
    String novaSenha
) {}