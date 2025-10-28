package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.dtos.PontoDTO;
import com.pedropetterini.calculadora_topografica.exceptions.PontoNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Ponto;
import com.pedropetterini.calculadora_topografica.repositories.LevantamentoRepository;
import com.pedropetterini.calculadora_topografica.repositories.PontoRepository;
import com.pedropetterini.calculadora_topografica.validators.PontoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PontoService {
    private final PontoRepository pontoRepository;
    private final PontoValidator validator;
    private final LevantamentoRepository levantamentoRepository;

    public Ponto salvarPonto(PontoDTO pontoDTO) {
        Ponto ponto = new Ponto();
        ponto.setLevantamento(levantamentoRepository.findById(pontoDTO.getLevantamentoId()).orElseThrow());
        ponto.setEstacao(pontoDTO.getEstacao());
        ponto.setNome(pontoDTO.getNome());
        ponto.setDistancia(pontoDTO.getDistancia());
        ponto.setAngulo(pontoDTO.getAngulo().toDecimal());

        if (pontoDTO.getReferencia() != null) {
            var aux = pontoRepository.findByNomeAndLevantamentoId(pontoDTO.getReferencia(), pontoDTO.getLevantamentoId()).orElseThrow();
            ponto.setReferencia(aux);
            ponto.setAzimute(aux.getAzimute() + ponto.getAngulo());
        }else{
            ponto.setAzimute(pontoDTO.getAzimute().toDecimal());
        }


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

    public void removerPonto(UUID id) {
        if(pontoRepository.existsById(id)) {
            pontoRepository.deleteById(id);
        }else{
            throw new PontoNotFoundException("Ponto não encontrado com o ID: " + id);
        }
    }

//    public Ponto alterarPonto(UUID id, PontoDTO ponto) {
//        if(pontoRepository.existsById(id)) {
//            Ponto oldPonto = pontoRepository.findById(id).get();
//            oldPonto.setAngulo(ponto.getAngulo());
//            oldPonto.setDistancia(ponto.getDistancia());
//            oldPonto.setNome(ponto.getNome());
//        }
//        return null;
//    }

}
