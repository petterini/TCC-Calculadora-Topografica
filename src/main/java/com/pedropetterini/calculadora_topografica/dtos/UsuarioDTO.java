package com.pedropetterini.calculadora_topografica.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class UsuarioDTO {

    private UUID id;


    private String nome;

    @NotBlank
    private String email;

    @NotBlank
    private String senha;

    private String status;
}
