package com.pedropetterini.calculadora_topografica.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LevantamentoDTO {

    private UUID id;

    @NotBlank
    private String nome;

    @NotBlank
    private String tipo;

    @NotNull
    private Double coordX;

    @NotNull
    private Double coordY;
}
