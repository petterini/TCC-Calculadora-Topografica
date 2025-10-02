package com.pedropetterini.calculadora_topografica.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/cadastrarUsuario")
    public String paginaCadastro(){
        return "cadastrarUsuario";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/selecionarMetodo")
    public String selecionarMetodo(){
        return "selecionarMetodo";
    }

    @GetMapping("/irradiacao")
    public String irradiacao(){
        return "irradiacao";
    }
}
