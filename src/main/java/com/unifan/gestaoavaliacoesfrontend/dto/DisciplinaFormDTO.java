package com.unifan.gestaoavaliacoesfrontend.dto;

public record DisciplinaFormDTO(
    String nome,
    String descricao,
    Integer cargaHoraria,
    String turno,
    Long cursoId
) {}