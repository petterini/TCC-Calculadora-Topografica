package com.pedropetterini.calculadora_topografica.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<ErrorField> errorFields = fieldErrors.stream().map(fe -> new ErrorField(fe.getField(), fe.getDefaultMessage())).collect(Collectors.toList());
        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error", errorFields);
    }
}
