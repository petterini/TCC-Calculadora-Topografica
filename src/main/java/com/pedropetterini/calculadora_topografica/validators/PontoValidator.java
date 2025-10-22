package com.pedropetterini.calculadora_topografica.validators;

import com.pedropetterini.calculadora_topografica.models.Ponto;
import com.pedropetterini.calculadora_topografica.repositories.PontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PontoValidator {
    private final PontoRepository pontoRepository;

    public void validar(Ponto ponto){

    }

    public void validar(List<Ponto> pontos){

    }
}
