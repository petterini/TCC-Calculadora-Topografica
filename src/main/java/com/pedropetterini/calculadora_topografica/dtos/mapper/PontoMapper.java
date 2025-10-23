package com.pedropetterini.calculadora_topografica.dtos.mapper;

import com.pedropetterini.calculadora_topografica.dtos.PontoDTO;
import com.pedropetterini.calculadora_topografica.models.Ponto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PontoMapper {
    private final ModelMapper modelMapper;

    public PontoMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Ponto toEntity(PontoDTO pontoDTO){
        Ponto ponto = modelMapper.map(pontoDTO, Ponto.class);
        return ponto;
    }

    public PontoDTO toDTO(Ponto ponto){
        PontoDTO pontoDTO = modelMapper.map(ponto, PontoDTO.class);
        return pontoDTO;
    }

    public List<PontoDTO> toDTOs(List<Ponto> pontos){
        return pontos.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
