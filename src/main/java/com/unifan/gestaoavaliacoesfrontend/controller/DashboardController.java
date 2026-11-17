package com.unifan.gestaoavaliacoesfrontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    @GetMapping({"/", "/dashboard"})
    public String exibirDashboard(HttpSession session, Model model) {
        String token = (String) session.getAttribute("TOKEN");
        if (token == null) {
            return "redirect:/login";
        }

        model.addAttribute("nome", session.getAttribute("USUARIO_NOME"));
        model.addAttribute("tipo", session.getAttribute("USUARIO_TIPO"));
        return "dashboard";
    }
}