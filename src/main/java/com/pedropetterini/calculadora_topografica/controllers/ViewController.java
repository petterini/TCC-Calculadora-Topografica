package com.pedropetterini.calculadora_topografica.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    @GetMapping("/cadastrar")
    public String paginaCadastro(){
        return "cadastrar";
    }
}
