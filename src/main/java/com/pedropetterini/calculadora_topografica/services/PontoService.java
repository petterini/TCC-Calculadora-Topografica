package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.exceptions.PontoNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Ponto;
import com.pedropetterini.calculadora_topografica.repositories.PontoRepository;
import com.pedropetterini.calculadora_topografica.validators.PontoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PontoService {
    private final PontoRepository pontoRepository;
    private final PontoValidator validator;

    public Ponto save(Ponto ponto) {
        return pontoRepository.save(ponto);
    }

    public List<Ponto> cadastrarListaDePontos(List<Ponto> pontos) {
        validator.validar(pontos);
        pontoRepository.saveAll(pontos);
        return pontos;
    }

    public Ponto cadastrarPonto(Ponto ponto) {
        validator.validar(ponto);
        return pontoRepository.save(ponto);
    }

    public List<Ponto> listarPontosPorLevantamento(UUID idLevantamento) {
        if(pontoRepository.existsByLevantamentoId(idLevantamento)) {
            return pontoRepository.findByLevantamentoId(idLevantamento);
        }

        throw new PontoNotFoundException("Pontos não encontrados com o levantamento ID: " + idLevantamento);
    }

    public Ponto getPontoById(UUID id) {
        if(pontoRepository.existsById(id)) {
            return pontoRepository.findById(id).get();
        }

        throw new PontoNotFoundException("Ponto não encontrado com o ID: " + id);
    }

}
