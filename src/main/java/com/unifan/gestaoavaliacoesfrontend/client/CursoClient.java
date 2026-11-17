package com.unifan.gestaoavaliacoesfrontend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.unifan.gestaoavaliacoesfrontend.dto.CursoDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.CursoFormDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.PageResponseDTO;

@Component
public class CursoClient {

    private final RestClient restClient;

    public CursoClient(@Value("${api.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public PageResponseDTO<CursoDTO> listar(String token, int page, int size) {
        return restClient.get()
                .uri("/cursos?page={page}&size={size}&sortBy=id&sortDir=asc", page, size)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public void criar(String token, CursoFormDTO dto) {
        restClient.post()
                .uri("/cursos")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    public void atualizar(String token, Long id, CursoFormDTO dto) {
        restClient.put()
                .uri("/cursos/{id}", id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    public void remover(String token, Long id) {
        restClient.delete()
                .uri("/cursos/{id}", id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity();
    }
}