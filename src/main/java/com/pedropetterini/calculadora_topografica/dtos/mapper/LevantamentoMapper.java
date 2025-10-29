package com.pedropetterini.calculadora_topografica.dtos.mapper;

import com.pedropetterini.calculadora_topografica.dtos.LevantamentoDTO;
import com.pedropetterini.calculadora_topografica.models.Levantamento;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LevantamentoMapper {
    private final ModelMapper modelMapper;

    public LevantamentoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Levantamento toEntity(LevantamentoDTO levantamentoDTODTO){
        Levantamento levantamento = modelMapper.map(levantamentoDTODTO, Levantamento.class);
        return levantamento;
    }

    public LevantamentoDTO toDTO(Levantamento levantamento){
        LevantamentoDTO levantamentoDTO = modelMapper.map(levantamento, LevantamentoDTO.class);
        return levantamentoDTO;
    }

    public List<LevantamentoDTO> toDTOs(List<Levantamento> levantamentos){
        return levantamentos.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
