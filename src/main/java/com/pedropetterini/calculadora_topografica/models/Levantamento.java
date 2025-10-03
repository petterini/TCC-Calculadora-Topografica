package com.pedropetterini.calculadora_topografica.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
    private UUID idUsuario;

    @NotNull
    private String tipo;

    @NotNull
    private Date data_criacao;

    private double area;

    private double perimetro;

    private int croqui;

}
