package com.unifan.gestaoavaliacoesfrontend.dto;

import java.time.LocalDateTime;

public record UsuarioDTO(
    Long id,
    String email,
    String nome,
    String tipo,
    LocalDateTime criadoEm
) {}