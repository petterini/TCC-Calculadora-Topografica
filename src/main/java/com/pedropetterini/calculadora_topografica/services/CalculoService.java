package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.models.Ponto;
import org.springframework.stereotype.Service;

@Service
public class CalculoService {
    public void calcularProjecoes(Ponto ponto) {
        double aux = ponto.getAzimute() * Math.PI / 180;

        ponto.setProjX(ponto.getDistancia() * Math.sin(aux));
        ponto.setProjY(ponto.getDistancia() * Math.cos(aux));
    }

    public void calcularCoordenadas(Ponto ponto, Ponto pOrigem) {
        if(ponto.getReferencia() != null){
            ponto.setCoordX(pOrigem.getCoordX() + ponto.getProjX());
            ponto.setCoordY(pOrigem.getCoordY() + ponto.getProjY());
        }
    }

    public void calcularTudo(Ponto ponto, Ponto p0) {
        calcularProjecoes(ponto);
        calcularCoordenadas(ponto, p0);
    }
}
