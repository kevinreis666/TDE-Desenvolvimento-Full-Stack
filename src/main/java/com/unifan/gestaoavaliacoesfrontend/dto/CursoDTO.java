package com.unifan.gestaoavaliacoesfrontend.dto;

import java.time.LocalDateTime;

public record CursoDTO(
    Long id,
    String nome,
    String descricao,
    String formato,
    LocalDateTime criadoEm
) {}