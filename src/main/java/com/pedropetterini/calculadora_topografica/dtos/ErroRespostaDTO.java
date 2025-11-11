package com.pedropetterini.calculadora_topografica.dtos;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErroRespostaDTO(int status, String message, List<ErroCampoDTO> erros) {

    public static ErroRespostaDTO respostaPadrao(String message){
        return new ErroRespostaDTO(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

    public static ErroRespostaDTO conflito(String message){
        return new ErroRespostaDTO(HttpStatus.CONFLICT.value(), message, List.of());
    }

    public static ErroRespostaDTO levantamentoNotFound(String message) {
        return new ErroRespostaDTO(HttpStatus.NOT_FOUND.value(), message, List.of());
    }

    public static ErroRespostaDTO usuarioDuplicado(String message) {
        return new ErroRespostaDTO(HttpStatus.CONFLICT.value(), message, List.of());
    }

    public static ErroRespostaDTO usuarioNotFound(String message) {
        return new ErroRespostaDTO(HttpStatus.NOT_FOUND.value(), message, List.of());
    }

    public static ErroRespostaDTO pontoNotFound(String message) {
        return new ErroRespostaDTO(HttpStatus.NOT_FOUND.value(), message, List.of());
    }
}
