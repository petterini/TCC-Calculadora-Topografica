package com.pedropetterini.calculadora_topografica.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/entrar")
    public String login(){
        return "entrar";
    }

    @GetMapping("/cadastro")
    public String cadastro(){
        return "cadastro";
    }

    @GetMapping("/listar")
    public String listar(){
        return "listar";
    }

    @GetMapping
    public String dashboard(){
        return "dashboard";
    }
}
