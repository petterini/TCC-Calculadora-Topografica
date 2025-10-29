package com.pedropetterini.calculadora_topografica.dtos;

import com.pedropetterini.calculadora_topografica.utils.Angulo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PontoDTO {

    private UUID levantamentoId;

    @NotBlank
    private String estacao;

    @NotBlank
    private String nome;

    @NotNull
    private Angulo angulo;

    @NotNull
    private double distancia;

    private Angulo azimute;

    private String referencia;

    private Double coordX;

    private Double coordY;

}
