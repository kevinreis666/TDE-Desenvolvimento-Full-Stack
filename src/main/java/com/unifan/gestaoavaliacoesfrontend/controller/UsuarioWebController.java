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

import com.unifan.gestaoavaliacoesfrontend.client.UsuarioClient;
import com.unifan.gestaoavaliacoesfrontend.dto.PageResponseDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.UsuarioCreateFormDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.UsuarioDTO;
import com.unifan.gestaoavaliacoesfrontend.dto.UsuarioUpdateFormDTO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuarios")
public class UsuarioWebController {

    private final UsuarioClient usuarioClient;

    public UsuarioWebController(UsuarioClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    @GetMapping
    public String listarUsuarios(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 HttpSession session,
                                 Model model) {
        String token = (String) session.getAttribute("TOKEN");
        String tipo = (String) session.getAttribute("USUARIO_TIPO");
        if (token == null) return "redirect:/login";
        if (!"ADMIN".equals(tipo)) return "redirect:/dashboard";

        try {
            PageResponseDTO<UsuarioDTO> pagina = usuarioClient.listar(token, page, size);
            model.addAttribute("pagina", pagina);
            model.addAttribute("usuarioLogadoId", session.getAttribute("USUARIO_ID"));
            model.addAttribute("tipo", tipo);
            model.addAttribute("nome", session.getAttribute("USUARIO_NOME"));
            return "usuarios";
        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.Forbidden e) {
            session.invalidate();
            return "redirect:/login";
        }
    }

    @PostMapping("/criar")
    public String criarUsuario(@RequestParam String nome,
                               @RequestParam String email,
                               @RequestParam String tipo,
                               @RequestParam String senha,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("TOKEN");
        if (token == null) return "redirect:/login";

        try {
            usuarioClient.criar(token, new UsuarioCreateFormDTO(email, nome, tipo, senha));
            redirectAttributes.addFlashAttribute("sucesso", "Usuário cadastrado com sucesso!");
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar usuário (verifique se o e-mail já existe ou se a senha tem no mínimo 6 caracteres).");
        }

        return "redirect:/usuarios";
    }

    @PostMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id,
                                @RequestParam String nome,
                                @RequestParam String email,
                                @RequestParam String tipo,
                                @RequestParam(required = false) String novaSenha,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("TOKEN");
        if (token == null) return "redirect:/login";

        String senhaTratada = (novaSenha != null && !novaSenha.isBlank()) ? novaSenha : null;
        UsuarioUpdateFormDTO dto = new UsuarioUpdateFormDTO(email, nome, tipo, null, senhaTratada);

        try {
            usuarioClient.atualizar(token, id, dto);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário atualizado com sucesso!");
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar usuário: verifique os dados informados.");
        }

        return "redirect:/usuarios";
    }

    @PostMapping("/remover/{id}")
    public String removerUsuario(@PathVariable Long id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("TOKEN");
        if (token == null) return "redirect:/login";

        try {
            usuarioClient.remover(token, id);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário removido com sucesso!");
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("erro", "Não foi possível remover o usuário (verifique se há questões ou avaliações vinculadas a ele).");
        }

        return "redirect:/usuarios";
    }

    @GetMapping("/perfil")
    public String exibirPerfil(HttpSession session, Model model) {
        String token = (String) session.getAttribute("TOKEN");
        Long usuarioId = (Long) session.getAttribute("USUARIO_ID");
        if (token == null || usuarioId == null) return "redirect:/login";

        try {
            UsuarioDTO usuario = usuarioClient.buscarPorId(token, usuarioId);
            model.addAttribute("usuario", usuario);
            model.addAttribute("nome", session.getAttribute("USUARIO_NOME"));
            model.addAttribute("tipo", session.getAttribute("USUARIO_TIPO"));
            return "perfil";
        } catch (HttpClientErrorException e) {
            session.invalidate();
            return "redirect:/login";
        }
    }

    @PostMapping("/perfil")
    public String atualizarPerfil(@RequestParam String nome,
                                  @RequestParam String email,
                                  @RequestParam(required = false) String senhaAntiga,
                                  @RequestParam(required = false) String novaSenha,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        String token = (String) session.getAttribute("TOKEN");
        Long usuarioId = (Long) session.getAttribute("USUARIO_ID");
        if (token == null || usuarioId == null) return "redirect:/login";

        String antiga = (senhaAntiga != null && !senhaAntiga.isBlank()) ? senhaAntiga : null;
        String nova = (novaSenha != null && !novaSenha.isBlank()) ? novaSenha : null;

        UsuarioUpdateFormDTO dto = new UsuarioUpdateFormDTO(email, nome, null, antiga, nova);

        try {
            UsuarioDTO atualizado = usuarioClient.atualizar(token, usuarioId, dto);
            session.setAttribute("USUARIO_NOME", atualizado.nome());
            redirectAttributes.addFlashAttribute("sucesso", "Perfil atualizado com sucesso!");
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar perfil (verifique se a senha atual está correta e se a nova senha tem no mínimo 6 caracteres).");
        }

        return "redirect:/usuarios/perfil";
    }
}