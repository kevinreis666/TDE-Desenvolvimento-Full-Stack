package com.unifan.gestaoavaliacoesfrontend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.unifan.gestaoavaliacoesfrontend.dto.PageResponseDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.UsuarioCreateFormDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.UsuarioDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.UsuarioUpdateFormDTO;

@Component
public class UsuarioClient {

    private final RestClient restClient;

    public UsuarioClient(@Value("${api.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public PageResponseDTO<UsuarioDTO> listar(String token, int page, int size) {
        return restClient.get()
                .uri("/usuarios?page={page}&size={size}&sortBy=id&sortDir=asc", page, size)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public UsuarioDTO buscarPorId(String token, Long id) {
        return restClient.get()
                .uri("/usuarios/{id}", id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .body(UsuarioDTO.class);
    }

    public void criar(String token, UsuarioCreateFormDTO dto) {
        restClient.post()
                .uri("/usuarios")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .toBodilessEntity();
    }

    public UsuarioDTO atualizar(String token, Long id, UsuarioUpdateFormDTO dto) {
        return restClient.put()
                .uri("/usuarios/{id}", id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .body(UsuarioDTO.class);
    }

    public void remover(String token, Long id) {
        restClient.delete()
                .uri("/usuarios/{id}", id)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .toBodilessEntity();
    }
}