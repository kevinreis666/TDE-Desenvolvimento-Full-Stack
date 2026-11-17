package com.unifan.gestaoavaliacoesfrontend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.unifan.gestaoavaliacoesfrontend.dto.DisciplinaDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.DisciplinaFormDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.PageResponseDTO;

@Component
public class DisciplinaClient {

    private final RestClient restClient;

    public DisciplinaClient(@Value("${api.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public PageResponseDTO<DisciplinaDTO> listar(String token, Long cursoId, int page, int size) {
        String uri = (cursoId != null)
                ? "/disciplinas?cursoId={cursoId}&page={page}&size={size}&sortBy=id&sortDir=asc"
                : "/disciplinas?page={page}&size={size}&sortBy=id&sortDir=asc";

        if (cursoId != null) {
            return restClient.get()
                    .uri(uri, cursoId, page, size)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        }

        return restClient.get()
                .uri(uri, page, size)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public void criar(String token, DisciplinaFormDTO dto) {
        restClient.post()
                .uri("/disciplinas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    public void atualizar(String token, Long id, DisciplinaFormDTO dto) {
        restClient.put()
                .uri("/disciplinas/{id}", id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    public void remover(String token, Long id) {
        restClient.delete()
                .uri("/disciplinas/{id}", id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity();
    }
}