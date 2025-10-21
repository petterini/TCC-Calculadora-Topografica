package com.pedropetterini.calculadora_topografica.controllers;

import com.pedropetterini.calculadora_topografica.exceptions.LevantamentoNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Levantamento;
import com.pedropetterini.calculadora_topografica.services.LevantamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class LevantamentoController {
    private LevantamentoService levantamentoService;

    @PostMapping("/cadastrarLevantamento")
    public String cadastrarLevantamento(Levantamento levantamento) {
        levantamentoService.cadastrar(levantamento);
        return null;
    }

    @GetMapping("/getLevantamentoById/{id}")
    public String getLevantamentoById(@RequestParam UUID id) {
        try{
            levantamentoService.getLevantamentoById(id);
            return "redirect:/levantamentos";
        }catch (LevantamentoNotFoundException e){
            return e.getMessage();
        }
    }

    

}
