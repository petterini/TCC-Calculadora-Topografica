package com.pedropetterini.calculadora_topografica.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table
@Data
public class Levantamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String nome;

    @NotNull
    @Column(name = "idusuario")
    private UUID idUsuario;

    @NotNull
    private String tipo;

    private LocalDate data_criacao;

    private double area;

    private double perimetro;

    private double coordX;

    private double coordY;

    private double erroAngular;

    private double erroLinearAbs;



}
