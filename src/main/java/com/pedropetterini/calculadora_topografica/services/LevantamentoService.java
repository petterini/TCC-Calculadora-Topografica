package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.exceptions.LevantamentoNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Levantamento;
import com.pedropetterini.calculadora_topografica.repositories.LevantamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class LevantamentoService {

    private final LevantamentoRepository levantamentoRepository;

    public Levantamento cadastrar(Levantamento levantamento) {
        return levantamentoRepository.save(levantamento);
    }

    public Levantamento getLevantamentoById(UUID id) {
        if(levantamentoRepository.existsById(id)) {
            return levantamentoRepository.findById(id).get();
        }else{
            throw new LevantamentoNotFoundException("Levantamento não encontrado");
        }
    }

    public List<Levantamento> getLevantamentoByUser(UUID id) {
        List<Levantamento> levantamentos = levantamentoRepository.getLevantamentoByIdUsuario(id);

        if(levantamentos.isEmpty()){
            throw new LevantamentoNotFoundException("Nenhum levantamento encontrado para o id " + id);
        }

        return levantamentos;
    }

    public List<Levantamento> getAllLevantamentos() {
        List<Levantamento> levantamentos = levantamentoRepository.findAll();

        if(levantamentos.isEmpty()){
            throw new LevantamentoNotFoundException("Nenhum levantamento cadastrado.");
        }

        return levantamentos;
    }


}
