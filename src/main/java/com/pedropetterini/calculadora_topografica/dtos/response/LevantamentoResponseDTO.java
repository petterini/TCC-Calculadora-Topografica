package com.pedropetterini.calculadora_topografica.dtos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pedropetterini.calculadora_topografica.models.Levantamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LevantamentoResponseDTO {
    private UUID id;

    private String nome;

    private UUID idUsuario;

    private String tipo;

    private Double area;

    private Double perimetro;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double erroAngular;

    public static LevantamentoResponseDTO toDTO(Levantamento levantamento) {
        LevantamentoResponseDTO responseDTO = new LevantamentoResponseDTO();
        responseDTO.setId(levantamento.getId());
        responseDTO.setNome(levantamento.getNome());
        responseDTO.setIdUsuario(levantamento.getIdUsuario());
        responseDTO.setTipo(levantamento.getTipo());
        responseDTO.setArea(levantamento.getArea());
        responseDTO.setPerimetro(levantamento.getPerimetro());

        if(levantamento.getTipo().equals("Caminhamento")){
            responseDTO.setErroAngular(levantamento.getErroAngular());
        }

        return responseDTO;
    }
}
