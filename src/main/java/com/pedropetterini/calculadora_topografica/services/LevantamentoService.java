package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.models.Levantamento;
import com.pedropetterini.calculadora_topografica.repositories.LevantamentoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LevantamentoService {

    private final LevantamentoRepository levantamentoRepository;

    public void cadastrar(Levantamento levantamento) {
        levantamentoRepository.save(levantamento);
    }
}
