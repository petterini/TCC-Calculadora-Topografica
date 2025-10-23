package com.pedropetterini.calculadora_topografica.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PontoDTO {
    @NotBlank
    private String nome;

    @NotNull
    private Double angulo;

    @NotNull
    private Double distancia;

    private Double azimute;

    public Double calcularAngulo(double grau, double minuto, double segundo) {
        this.angulo = grau + minuto/60 + segundo/3600;
        return angulo;
    }

    public Double calcularAzimute(double grau, double minuto, double segundo) {
        this.azimute = grau + minuto/60 + segundo/3600;
        return azimute;
    }
}
