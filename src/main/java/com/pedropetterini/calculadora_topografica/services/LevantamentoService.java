package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.dtos.LevantamentoDTO;
import com.pedropetterini.calculadora_topografica.dtos.mapper.LevantamentoMapper;
import com.pedropetterini.calculadora_topografica.dtos.response.LevantamentoResponseDTO;
import com.pedropetterini.calculadora_topografica.exceptions.LevantamentoNotFoundException;
import com.pedropetterini.calculadora_topografica.exceptions.UserNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Levantamento;
import com.pedropetterini.calculadora_topografica.models.Usuario;
import com.pedropetterini.calculadora_topografica.repositories.LevantamentoRepository;
import com.pedropetterini.calculadora_topografica.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LevantamentoService {

    private final LevantamentoRepository levantamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CalculoService calculoService;
    private final LevantamentoMapper levantamentoMapper;

    public LevantamentoResponseDTO cadastrar(LevantamentoDTO levantamentoDTO) {

        Usuario user = usuarioRepository.findById(levantamentoDTO.getIdUsuario()).orElseThrow(() ->
                new UserNotFoundException("Usuário não encontrado."));

        Levantamento levantamento = levantamentoMapper.toEntity(levantamentoDTO);
        levantamento.setUsuario(user);
        levantamento.setData_criacao(LocalDate.now());

        return LevantamentoResponseDTO.toDTO(levantamentoRepository.save(levantamento));
    }

    public LevantamentoResponseDTO getLevantamentoById(UUID id) {
        Levantamento lev = levantamentoRepository.findById(id).orElseThrow(() ->
                new LevantamentoNotFoundException("Levantamento não encontrado."));

        return LevantamentoResponseDTO.toDTO(lev);

    }

    public List<LevantamentoResponseDTO> getLevantamentoByUser(UUID id) {
        List<Levantamento> levantamentos = levantamentoRepository.findLevantamentoByUsuarioId(id);

        if (levantamentos.isEmpty()) {
            throw new LevantamentoNotFoundException("Nenhum levantamento encontrado para o id " + id);
        }

        return LevantamentoResponseDTO.toDTOs(levantamentos);
    }

    public List<LevantamentoResponseDTO> getAllLevantamentos() {
        List<Levantamento> levantamentos = levantamentoRepository.findAll();

        if (levantamentos.isEmpty()) {
            throw new LevantamentoNotFoundException("Nenhum levantamento cadastrado.");
        }

        return LevantamentoResponseDTO.toDTOs(levantamentos);
    }

    public LevantamentoResponseDTO calcular(UUID idLevantamento) {
        if (levantamentoRepository.existsById(idLevantamento)) {
            Levantamento levantamento = levantamentoRepository.getLevantamentoById(idLevantamento);

            if (levantamento.getTipo().equals("Irradiação")) {
                calculoService.calcularAreaEPerimetro(levantamento);
            } else if (levantamento.getTipo().equals("Caminhamento")) {
                calculoService.calcularErroAngular(levantamento);
                calculoService.calcularCaminhamento(levantamento);
                calculoService.calcularArea(levantamento);
            } else if (levantamento.getTipo().equals("Caminhamento Irradiado")) {
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

    public LevantamentoResponseDTO calcular(UUID idLevantamento, List<String> nomes) {
        if (levantamentoRepository.existsById(idLevantamento)) {
            Levantamento lev = calculoService.calcularAreaEPerimetroPorListaDePontos(levantamentoRepository.getLevantamentoById(idLevantamento), nomes);
            return LevantamentoResponseDTO.toDTO(lev);

        } else {
            throw new LevantamentoNotFoundException("Levantamento não encontrado para o id " + idLevantamento);
        }
    }

    public LevantamentoResponseDTO atualizarLevantamento(LevantamentoDTO levantamentoDTO) {
        Levantamento levantamento = levantamentoRepository.findById(levantamentoDTO.getId()).orElseThrow(() ->
                new LevantamentoNotFoundException("Levantamento não encontrado"));

        levantamento.setNome(levantamentoDTO.getNome());

        return LevantamentoResponseDTO.toDTO(levantamento);
    }

    public void deletarLevantamento(UUID idLevantamento) {
        if (levantamentoRepository.existsById(idLevantamento)) {
            levantamentoRepository.deleteById(idLevantamento);
        }
        throw new LevantamentoNotFoundException("Levantamento não encontrado.");
    }
}

