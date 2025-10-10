package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.models.Ponto;
import com.pedropetterini.calculadora_topografica.repositories.PontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
@RequiredArgsConstructor
public class PontoService {
    private final PontoRepository pontoRepository;

    public Ponto save(Ponto ponto) {
        return pontoRepository.save(ponto);
    }

    public LinkedList<Ponto> save(LinkedList<Ponto> pontos) {
        pontoRepository.saveAll(pontos);
        return pontos;
    }
}
