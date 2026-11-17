package com.unifan.gestaoavaliacoesfrontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unifan.gestaoavaliacoesfrontend.client.CursoClient;
import com.unifan.gestaoavaliacoesfrontend.dto.CursoDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.CursoFormDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.PageResponseDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cursos")
public class CursoWebController {

    private final CursoClient cursoClient;

    public CursoWebController(CursoClient cursoClient) {
        this.cursoClient = cursoClient;
    }

    @GetMapping
    public String listarCursos(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               HttpSession session,
                               Model model) {
        String token = (String) session.getAttribute("TOKEN");
        if (token == null) return "redirect:/login";

        try {
            PageResponseDTO<CursoDTO> pagina = cursoClient.listar(token, page, size);
            model.addAttribute("pagina", pagina);
            model.addAttribute("tipo", session.getAttribute("USUARIO_TIPO"));
            model.addAttribute("nome", session.getAttribute("USUARIO_NOME"));
            return "cursos";
        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.Forbidden e) {
            session.invalidate();
            return "redirect:/login";
        }
    }

    @PostMapping("/salvar")
    public String salvarCurso(@RequestParam(required = false) Long id,
                              @RequestParam String nome,
                              @RequestParam(required = false) String descricao,
                              @RequestParam String formato,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("TOKEN");
        if (token == null) return "redirect:/login";

        CursoFormDTO dto = new CursoFormDTO(nome, descricao, formato);

        try {
            if (id == null) {
                cursoClient.criar(token, dto);
                redirectAttributes.addFlashAttribute("sucesso", "Curso criado com sucesso!");
            } else {
                cursoClient.atualizar(token, id, dto);
                redirectAttributes.addFlashAttribute("sucesso", "Curso atualizado com sucesso!");
            }
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar curso: verifique os dados ou suas permissões.");
        }

        return "redirect:/cursos";
    }

    @PostMapping("/remover/{id}")
    public String removerCurso(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("TOKEN");
        if (token == null) return "redirect:/login";

        try {
            cursoClient.remover(token, id);
            redirectAttributes.addFlashAttribute("sucesso", "Curso removido com sucesso!");
        } catch (HttpClientErrorException e) {
            // Captura bloqueios de regra de negócio (ex: curso com disciplinas ou usuários vinculados)
            redirectAttributes.addFlashAttribute("erro", "Não foi possível remover o curso (verifique se há disciplinas ou usuários vinculados).");
        }

        return "redirect:/cursos";
    }
}