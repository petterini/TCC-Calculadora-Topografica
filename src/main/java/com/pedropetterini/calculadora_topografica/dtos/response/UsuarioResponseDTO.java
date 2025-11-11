package com.pedropetterini.calculadora_topografica.dtos.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class UsuarioResponseDTO {

    private UUID id;
    private String nome;
    private String email;
    private String status;
}
