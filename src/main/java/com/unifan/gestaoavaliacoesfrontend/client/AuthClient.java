package com.unifan.gestaoavaliacoesfrontend.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.unifan.gestaoavaliacoesfrontend.dto.LoginRequestDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.LoginResponseDTO;

@Component
public class AuthClient {

    private final RestClient restClient;

    public AuthClient(@Value("${api.base-url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public LoginResponseDTO autenticar(String email, String senha) {
        LoginRequestDTO request = new LoginRequestDTO(email, senha);

        return restClient.post()
                .uri("/auth/login") // Confirme se a rota de login no seu AuthController é /auth/login
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(LoginResponseDTO.class);
    }
}