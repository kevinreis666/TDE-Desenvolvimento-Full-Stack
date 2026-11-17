package com.unifan.gestaoavaliacoesfrontend.dto;

public record UsuarioCreateFormDTO(
    String email,
    String nome,
    String tipo,
    String senha
) {}