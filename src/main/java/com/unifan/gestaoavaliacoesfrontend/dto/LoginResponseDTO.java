package com.unifan.gestaoavaliacoesfrontend.dto;

public record LoginResponseDTO(
    String mensagem,
    String token,
    String tokenType,
    Long id,
    String email,
    String nome,
    String tipo
) {}