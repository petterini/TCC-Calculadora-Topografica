package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.dtos.PontoDTO;
import com.pedropetterini.calculadora_topografica.dtos.response.PontoResponseDTO;
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
        ponto.setLevantamento(levantamentoRepository.findById(pontoDTO.getLevantamentoId()).orElseThrow());
        ponto.setEstacao(pontoDTO.getEstacao());
        ponto.setNome(pontoDTO.getNome());
        ponto.setDistancia(pontoDTO.getDistancia());
        ponto.setAnguloLido(pontoDTO.getAngulo().toDecimal());

        if(ponto.getLevantamento().getTipo().equals("Irradiação")){
            if (pontoDTO.getReferencia() != null) {
                var aux = pontoRepository.findByNomeAndLevantamentoId(pontoDTO.getReferencia(), pontoDTO.getLevantamentoId()).orElseThrow();
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
                var aux = pontoRepository.findByNomeAndLevantamentoId(pontoDTO.getReferencia(), pontoDTO.getLevantamentoId()).orElseThrow();
                ponto.setReferencia(aux);
            }else{
                ponto.setAzimute(pontoDTO.getAzimute().toDecimal());
            }
        }else if(ponto.getLevantamento().getTipo().equals("Caminhamento Irradiado")){
            if(pontoDTO.getReferencia() == null) {
                ponto.setAzimuteRe(pontoDTO.getAzimuteRe().toDecimal());
            }else{
                var aux = pontoRepository.findByNomeAndLevantamentoId(pontoDTO.getReferencia(), pontoDTO.getLevantamentoId()).orElseThrow();
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
            List<Ponto> pontos = pontoRepository.findByLevantamentoId(idLevantamento);
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
