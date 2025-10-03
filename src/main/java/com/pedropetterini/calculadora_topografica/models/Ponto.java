package com.pedropetterini.calculadora_topografica.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table
public class Ponto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private UUID id_levantamento;

    @NotNull
    private double angulo;

    @NotNull
    private double distancia;

    @NotNull
    private double azimute;
}
