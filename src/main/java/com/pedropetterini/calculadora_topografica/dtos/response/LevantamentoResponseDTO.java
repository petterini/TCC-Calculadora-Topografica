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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double erroLinearAbs;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double erroLinearRelativo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mensagemAviso;

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
            responseDTO.setErroLinearAbs(levantamento.getErroLinearAbs());
            responseDTO.setErroLinearRelativo(levantamento.getErroLinearAbs() / (levantamento.getPerimetro() / 1000));

            if(responseDTO.getErroAngular() > 1 || responseDTO.getErroAngular() < -1){
                responseDTO.setMensagemAviso("O erro angular está alto, é recomendado refazer o levantamento.");
            }
            if(responseDTO.getErroLinearRelativo() > 1){
                responseDTO.setMensagemAviso("O erro linear está alto, é recomendado refazer o levantamento.");
            }
        }

        return responseDTO;
    }
}
