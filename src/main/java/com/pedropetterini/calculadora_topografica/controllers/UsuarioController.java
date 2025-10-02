package com.pedropetterini.calculadora_topografica.controllers;

import com.pedropetterini.calculadora_topografica.models.Usuario;
import com.pedropetterini.calculadora_topografica.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrarUsuario")
    public String cadastrarUsuario(Usuario usuario, Model model) {
        try {
            usuarioService.cadastrarUsuario(usuario);
            model.addAttribute("mensagem", "Usuario cadastrado com sucesso!");
        }catch (RuntimeException e){
            model.addAttribute("mensagem", e.getMessage());
        }
        return "login";
    }
}
