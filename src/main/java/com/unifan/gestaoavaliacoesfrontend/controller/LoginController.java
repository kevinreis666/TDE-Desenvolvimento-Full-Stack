package com.unifan.gestaoavaliacoesfrontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import com.unifan.gestaoavaliacoesfrontend.client.AuthClient;
import com.unifan.gestaoavaliacoesfrontend.dto.LoginResponseDTO;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final AuthClient authClient;

    public LoginController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @GetMapping("/login")
    public String exibirLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String processarLogin(@RequestParam String email,
                                 @RequestParam String senha,
                                 HttpSession session,
                                 Model model) {
        try {
            LoginResponseDTO response = authClient.autenticar(email, senha);
            
            session.setAttribute("TOKEN", response.token());
            session.setAttribute("USUARIO_ID", response.id());
            session.setAttribute("USUARIO_NOME", response.nome());
            session.setAttribute("USUARIO_TIPO", response.tipo());

            return "redirect:/dashboard";
        } catch (HttpClientErrorException e) {
            model.addAttribute("erro", "E-mail ou senha inválidos.");
            return "login";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao conectar com o servidor backend.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
