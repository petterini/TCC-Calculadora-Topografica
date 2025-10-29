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

    @NotNull
    private UUID idUsuario;

    @NotBlank
    private String tipo;
}
