package com.pedropetterini.calculadora_topografica.models.enums;

public enum UsuarioRole {
    ADMIN("admin"),
    USER("user");

    private String role;

    UsuarioRole(String role) {
        this.role = role;
    }
}
