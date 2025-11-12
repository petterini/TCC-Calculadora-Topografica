package com.pedropetterini.calculadora_topografica.exceptions;

public class UserNotLoggedException extends RuntimeException {
    public UserNotLoggedException(String message) {
        super(message);
    }
}
