package com.pedropetterini.calculadora_topografica.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Angulo {

    private double graus;
    private double minutos;
    private double segundos;

    public double toDecimal() {
        return graus + minutos / 60.0 + segundos / 3600.0;
    }

    public static Angulo fromDecimal(double decimal) {
        int g = (int) decimal;
        double resto = (decimal - g) * 60;
        int m = (int) resto;
        double s = (resto - m) * 60;
        return new Angulo(g, m, s);
    }
}
