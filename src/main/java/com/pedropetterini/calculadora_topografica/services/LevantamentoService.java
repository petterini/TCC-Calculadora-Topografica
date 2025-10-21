package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.exceptions.LevantamentoNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Levantamento;
import com.pedropetterini.calculadora_topografica.repositories.LevantamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.UUID;

@RequiredArgsConstructor
public class LevantamentoService {

    private final LevantamentoRepository levantamentoRepository;

    public void cadastrar(Levantamento levantamento) {
        levantamentoRepository.save(levantamento);
    }

    public Levantamento getLevantamentoById(UUID id) {
        if(levantamentoRepository.existsById(id)) {
            return levantamentoRepository.findById(id).get();
        }else{
            throw new LevantamentoNotFoundException("Levantamento não encontrado");
        }
    }
}
