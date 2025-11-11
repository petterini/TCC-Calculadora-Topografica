package com.pedropetterini.calculadora_topografica.exceptions;

public class UserDuplicatedException extends RuntimeException {
    public UserDuplicatedException(String message) {
        super(message);
    }
}
