package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.dtos.PontoDTO;
import com.pedropetterini.calculadora_topografica.dtos.response.PontoResponseDTO;
import com.pedropetterini.calculadora_topografica.exceptions.LevantamentoNotFoundException;
import com.pedropetterini.calculadora_topografica.exceptions.PontoNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Ponto;
import com.pedropetterini.calculadora_topografica.repositories.LevantamentoRepository;
import com.pedropetterini.calculadora_topografica.repositories.PontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PontoService {
    private final PontoRepository pontoRepository;
    private final LevantamentoRepository levantamentoRepository;
    private final CalculoService calculoService;

    public PontoResponseDTO salvarPonto(PontoDTO pontoDTO) {
        Ponto ponto = new Ponto();
        ponto.setLevantamento(levantamentoRepository.findById(pontoDTO.getLevantamentoId()).orElseThrow(() ->
                new LevantamentoNotFoundException("Levantamento não encontrado.")));
        ponto.setEstacao(pontoDTO.getEstacao());
        ponto.setNome(pontoDTO.getNome());
        ponto.setDistancia(pontoDTO.getDistancia());
        ponto.setAnguloLido(pontoDTO.getAngulo().toDecimal());

        if(ponto.getLevantamento().getTipo().equals("Irradiação")){
            if (pontoDTO.getReferencia() != null) {
                var aux = pontoRepository.findByNomeAndLevantamentoId(pontoDTO.getReferencia(), pontoDTO.getLevantamentoId()).orElseThrow(() ->
                        new PontoNotFoundException("Referência não encontrada."));
                ponto.setReferencia(aux);
                ponto.setAnguloHz(ponto.getAnguloLido() - aux.getAnguloLido());
                ponto.setAzimute(aux.getAzimute() + ponto.getAnguloHz());
            }else{
                ponto.setAzimute(pontoDTO.getAzimute().toDecimal());
                ponto.setAnguloHz(0);
            }

            calculoService.calcularProjecoes(ponto);
            calculoService.calcularCoordenadas(ponto, ponto.getLevantamento());
        }else if(ponto.getLevantamento().getTipo().equals("Caminhamento")){
            if(pontoDTO.getReferencia() != null) {
                var aux = pontoRepository.findByNomeAndLevantamentoId(pontoDTO.getReferencia(), pontoDTO.getLevantamentoId()).orElseThrow(() ->
                        new PontoNotFoundException("Referência não encontrada."));
                ponto.setReferencia(aux);
            }else{
                ponto.setAzimute(pontoDTO.getAzimute().toDecimal());
            }
        }else if(ponto.getLevantamento().getTipo().equals("Caminhamento Irradiado")){
            if(pontoDTO.getReferencia() == null) {
                if(ponto.getAzimuteRe() != 0){
                    ponto.setAzimuteRe(pontoDTO.getAzimuteRe().toDecimal());
                }else{
                    ponto.setAzimute(pontoDTO.getAzimute().toDecimal());
                }
            }else{
                var aux = pontoRepository.findByNomeAndLevantamentoId(pontoDTO.getReferencia(), pontoDTO.getLevantamentoId()).orElseThrow(() ->
                        new PontoNotFoundException("Referência não encontrada."));
                ponto.setReferencia(aux);
            }
        }


        Ponto pontoResponse = pontoRepository.save(ponto);
        return PontoResponseDTO.toDto(pontoResponse);
    }

    public List<PontoResponseDTO> salvarListaDePontos(List<PontoDTO> pontos) {
        List <PontoResponseDTO> pontosDTO = new ArrayList<>();
        for (PontoDTO ponto : pontos) {
            pontosDTO.add(salvarPonto(ponto));
        }

        return pontosDTO;
    }

    public List<PontoResponseDTO> listarPontosPorLevantamento(UUID idLevantamento) {
        if(pontoRepository.existsByLevantamentoId(idLevantamento)) {
            List<Ponto> pontos = pontoRepository.findByLevantamentoIdOrderByNomeAsc(idLevantamento);
            return PontoResponseDTO.toDto(pontos);
        }
        throw new PontoNotFoundException("Pontos não encontrados com o levantamento ID: " + idLevantamento);
    }

    public Ponto getPontoById(UUID id) {
        if(pontoRepository.existsById(id)) {
            return pontoRepository.findById(id).get();
        }

        throw new PontoNotFoundException("Ponto não encontrado com o ID: " + id);
    }

    public void deletarPonto(UUID id) {
        if(pontoRepository.existsById(id)) {
            pontoRepository.deleteById(id);
        }else{
            throw new PontoNotFoundException("Ponto não encontrado com o ID: " + id);
        }
    }

    public void deletarPorIdLevantamento(UUID idLevantamento) {
        pontoRepository.deleteByLevantamentoId(idLevantamento);
    }

    public PontoResponseDTO atualizarPonto(PontoDTO pontoDTO) {
        Ponto ponto = pontoRepository.findById(pontoDTO.getId())
                .orElseThrow(() -> new PontoNotFoundException("Ponto não encontrado."));

        ponto.setEstacao(pontoDTO.getEstacao());
        ponto.setNome(pontoDTO.getNome());
        ponto.setDistancia(pontoDTO.getDistancia());
        ponto.setAnguloLido(pontoDTO.getAngulo().toDecimal());

        var levantamento = levantamentoRepository.findById(pontoDTO.getLevantamentoId())
                .orElseThrow(() -> new LevantamentoNotFoundException("Levantamento não encontrado."));
        ponto.setLevantamento(levantamento);

        if (levantamento.getTipo().equals("Irradiação")) {
            if (pontoDTO.getReferencia() != null) {
                var aux = pontoRepository.findByNomeAndLevantamentoId(pontoDTO.getReferencia(), pontoDTO.getLevantamentoId())
                        .orElseThrow(() -> new PontoNotFoundException("Referência não encontrada."));
                ponto.setReferencia(aux);
                ponto.setAnguloHz(ponto.getAnguloLido() - aux.getAnguloLido());
                ponto.setAzimute(aux.getAzimute() + ponto.getAnguloHz());
            } else {
                ponto.setAzimute(pontoDTO.getAzimute().toDecimal());
                ponto.setAnguloHz(0);
                ponto.setReferencia(null);
            }

            calculoService.calcularProjecoes(ponto);
            calculoService.calcularCoordenadas(ponto, levantamento);

        } else if (levantamento.getTipo().equals("Caminhamento")) {
            if (pontoDTO.getReferencia() != null) {
                var aux = pontoRepository.findByNomeAndLevantamentoId(pontoDTO.getReferencia(), pontoDTO.getLevantamentoId())
                        .orElseThrow(() -> new PontoNotFoundException("Referência não encontrada."));
                ponto.setReferencia(aux);
            } else {
                ponto.setAzimute(pontoDTO.getAzimute().toDecimal());
                ponto.setReferencia(null);
            }

        } else if (levantamento.getTipo().equals("Caminhamento Irradiado")) {
            if (pontoDTO.getReferencia() == null) {
                if (pontoDTO.getAzimuteRe() != null) {
                    ponto.setAzimuteRe(pontoDTO.getAzimuteRe().toDecimal());
                } else {
                    ponto.setAzimute(pontoDTO.getAzimute().toDecimal());
                }
                ponto.setReferencia(null);
            } else {
                var aux = pontoRepository.findByNomeAndLevantamentoId(pontoDTO.getReferencia(), pontoDTO.getLevantamentoId())
                        .orElseThrow(() -> new PontoNotFoundException("Referência não encontrada."));
                ponto.setReferencia(aux);
            }
        }

        Ponto pontoAtualizado = pontoRepository.save(ponto);
        return PontoResponseDTO.toDto(pontoAtualizado);
    }
}
