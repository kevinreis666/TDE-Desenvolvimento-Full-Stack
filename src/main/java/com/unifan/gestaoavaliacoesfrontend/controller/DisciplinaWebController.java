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
import com.unifan.gestaoavaliacoesfrontend.client.DisciplinaClient;
import com.unifan.gestaoavaliacoesfrontend.dto.CursoDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.DisciplinaDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.DisciplinaFormDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.PageResponseDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/disciplinas")
public class DisciplinaWebController {

    private final DisciplinaClient disciplinaClient;
    private final CursoClient cursoClient;

    public DisciplinaWebController(DisciplinaClient disciplinaClient, CursoClient cursoClient) {
        this.disciplinaClient = disciplinaClient;
        this.cursoClient = cursoClient;
    }

    @GetMapping
    public String listarDisciplinas(@RequestParam(required = false) Long cursoId,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    HttpSession session,
                                    Model model) {
        String token = (String) session.getAttribute("TOKEN");
        if (token == null) return "redirect:/login";

        try {
            PageResponseDTO<DisciplinaDTO> pagina = disciplinaClient.listar(token, cursoId, page, size);
            // Busca os cursos para popular o filtro e o select do modal
            PageResponseDTO<CursoDTO> cursosPagina = cursoClient.listar(token, 0, 100);

            model.addAttribute("pagina", pagina);
            model.addAttribute("cursos", cursosPagina.content());
            model.addAttribute("cursoIdSelecionado", cursoId);
            model.addAttribute("tipo", session.getAttribute("USUARIO_TIPO"));
            model.addAttribute("nome", session.getAttribute("USUARIO_NOME"));
            return "disciplinas";
        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.Forbidden e) {
            session.invalidate();
            return "redirect:/login";
        }
    }

    @PostMapping("/salvar")
    public String salvarDisciplina(@RequestParam(required = false) Long id,
                                   @RequestParam String nome,
                                   @RequestParam(required = false) String descricao,
                                   @RequestParam Integer cargaHoraria,
                                   @RequestParam String turno,
                                   @RequestParam Long cursoId,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("TOKEN");
        if (token == null) return "redirect:/login";

        DisciplinaFormDTO dto = new DisciplinaFormDTO(nome, descricao, cargaHoraria, turno, cursoId);

        try {
            if (id == null) {
                disciplinaClient.criar(token, dto);
                redirectAttributes.addFlashAttribute("sucesso", "Disciplina criada com sucesso!");
            } else {
                disciplinaClient.atualizar(token, id, dto);
                redirectAttributes.addFlashAttribute("sucesso", "Disciplina atualizada com sucesso!");
            }
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar disciplina: verifique os dados ou suas permissões.");
        }

        return "redirect:/disciplinas";
    }

    @PostMapping("/remover/{id}")
    public String removerDisciplina(@PathVariable Long id,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("TOKEN");
        if (token == null) return "redirect:/login";

        try {
            disciplinaClient.remover(token, id);
            redirectAttributes.addFlashAttribute("sucesso", "Disciplina removida com sucesso!");
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("erro", "Não foi possível remover a disciplina (verifique se há questões, avaliações ou usuários vinculados).");
        }

        return "redirect:/disciplinas";
    }
}