package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.dtos.LevantamentoDTO;
import com.pedropetterini.calculadora_topografica.dtos.mapper.LevantamentoMapper;
import com.pedropetterini.calculadora_topografica.dtos.response.LevantamentoResponseDTO;
import com.pedropetterini.calculadora_topografica.exceptions.LevantamentoNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Levantamento;
import com.pedropetterini.calculadora_topografica.repositories.LevantamentoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LevantamentoService {

    private final LevantamentoRepository levantamentoRepository;
    private final CalculoService calculoService;
    private final LevantamentoMapper levantamentoMapper;

    public Levantamento cadastrar(LevantamentoDTO levantamentoDTO) {
        Levantamento levantamento = levantamentoMapper.toEntity(levantamentoDTO);
        levantamento.setData_criacao(LocalDate.now());

        return levantamentoRepository.save(levantamento);
    }

    public Levantamento getLevantamentoById(UUID id) {
        if (levantamentoRepository.existsById(id)) {
            return levantamentoRepository.findById(id).get();
        } else {
            throw new LevantamentoNotFoundException("Levantamento não encontrado");
        }
    }

    public List<Levantamento> getLevantamentoByUser(UUID id) {
        List<Levantamento> levantamentos = levantamentoRepository.getLevantamentoByIdUsuario(id);

        if (levantamentos.isEmpty()) {
            throw new LevantamentoNotFoundException("Nenhum levantamento encontrado para o id " + id);
        }

        return levantamentos;
    }

    public List<Levantamento> getAllLevantamentos() {
        List<Levantamento> levantamentos = levantamentoRepository.findAll();

        if (levantamentos.isEmpty()) {
            throw new LevantamentoNotFoundException("Nenhum levantamento cadastrado.");
        }

        return levantamentos;
    }


    public Levantamento alterarLevantamento(UUID id, @Valid LevantamentoDTO levantamentoDTO) {
        Levantamento levantamento = getLevantamentoById(id);
        levantamento.setNome(levantamentoDTO.getNome());
        levantamento.setIdUsuario(levantamentoDTO.getIdUsuario());
        levantamento.setTipo(levantamentoDTO.getTipo());

        return levantamentoRepository.save(levantamento);
    }

    public LevantamentoResponseDTO calcular(UUID idLevantamento) {
        if (levantamentoRepository.existsById(idLevantamento)) {
            Levantamento levantamento = levantamentoRepository.getLevantamentoById(idLevantamento);

            if(levantamento.getTipo().equals("Irradiação")){
                calculoService.calcularAreaEPerimetro(levantamento);
            }else if(levantamento.getTipo().equals("Caminhamento")){
                calculoService.calcularErroAngular(levantamento);
                calculoService.calcularCaminhamento(levantamento);
                calculoService.calcularArea(levantamento);
            }else if(levantamento.getTipo().equals("Caminhamento Irradiado")){
                calculoService.calcularErroAngular(levantamento);
                calculoService.calcularCaminhamento(levantamento);
                calculoService.calcularPontosIrradiados(levantamento);
            }

            levantamentoRepository.save(levantamento);
            return LevantamentoResponseDTO.toDTO(levantamento);
        } else {
            throw new LevantamentoNotFoundException("Levantamento não encontrado para o id " + idLevantamento);
        }
    }
}
