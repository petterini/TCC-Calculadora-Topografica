package com.pedropetterini.calculadora_topografica.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pedropetterini.calculadora_topografica.models.Ponto;
import lombok.Data;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class PontoResponseDTO {
    private UUID id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String estacao;

    private String nome;
    private double anguloLido;
    private double anguloHz;
    private double distancia;
    private double azimute;
    private double projX;
    private double projY;
    private double coordX;
    private double coordY;
    private String referencia;
    private String levantamento;

    public static PontoResponseDTO toDto(Ponto ponto) {
        PontoResponseDTO dto = new PontoResponseDTO();
        dto.setId(ponto.getId());
        dto.setEstacao(ponto.getEstacao());
        dto.setNome(ponto.getNome());
        dto.setAnguloLido(ponto.getAnguloLido());
        dto.setAnguloHz(ponto.getAnguloHz());
        dto.setDistancia(ponto.getDistancia());
        dto.setAzimute(ponto.getAzimute());
        dto.setProjX(ponto.getProjX());
        dto.setProjY(ponto.getProjY());
        dto.setCoordX(ponto.getCoordX());
        dto.setCoordY(ponto.getCoordY());
        dto.setLevantamento(ponto.getLevantamento().getNome());

        if(ponto.getReferencia() != null) {
            dto.setReferencia(ponto.getReferencia().getNome());
        }else {
            dto.setReferencia(null);
        }

        return dto;
    }

    public static List<PontoResponseDTO> toDto(List<Ponto> pontos) {
        return pontos.stream()
                .map(PontoResponseDTO::toDto)
                .collect(Collectors.toList());
    }
}
