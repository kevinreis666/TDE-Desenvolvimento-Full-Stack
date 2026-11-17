package com.unifan.gestaoavaliacoesfrontend.dto;

import java.time.LocalDateTime;

public record DisciplinaDTO(
    Long id,
    String nome,
    String descricao,
    Integer cargaHoraria,
    String turno,
    Long cursoId,
    String cursoNome,
    LocalDateTime criadoEm
) {}