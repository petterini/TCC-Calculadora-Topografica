package com.pedropetterini.calculadora_topografica.models;

import com.pedropetterini.calculadora_topografica.repositories.LevantamentoRepository;
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

    @ManyToOne
    @JoinColumn(name = "id_levantamento", nullable = false)
    @NotNull
    private Levantamento levantamento;

    @NotBlank
    private String estacao;

    @NotBlank
    private String nome;

    @NotNull
    private double angulo;

    @NotNull
    private double distancia;

    @NotNull
    private double azimute;

    @ManyToOne
    @JoinColumn(name = "id_referencia", nullable = true)
    private Ponto referencia;

}
