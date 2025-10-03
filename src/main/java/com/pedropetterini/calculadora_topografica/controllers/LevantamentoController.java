package com.pedropetterini.calculadora_topografica.controllers;

import com.pedropetterini.calculadora_topografica.models.Levantamento;
import com.pedropetterini.calculadora_topografica.services.LevantamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LevantamentoController {
    private LevantamentoService levantamentoService;

    @PostMapping("/cadastrarLevantamento")
    public String cadastrarLevantamento(Levantamento levantamento) {
        levantamentoService.cadastrar(levantamento);
        return null;
    }

}
