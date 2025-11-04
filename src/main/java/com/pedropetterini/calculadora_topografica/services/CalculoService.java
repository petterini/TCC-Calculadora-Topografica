package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.dtos.PontoDTO;
import com.pedropetterini.calculadora_topografica.exceptions.LevantamentoNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Levantamento;
import com.pedropetterini.calculadora_topografica.models.Ponto;
import com.pedropetterini.calculadora_topografica.repositories.LevantamentoRepository;
import com.pedropetterini.calculadora_topografica.repositories.PontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculoService {
    private final PontoRepository pontoRepository;

    public void calcularProjecoes(Ponto ponto) {
        double aux = ponto.getAzimute() * Math.PI / 180;

        ponto.setProjX(ponto.getDistancia() * Math.sin(aux));
        ponto.setProjY(ponto.getDistancia() * Math.cos(aux));
    }

    public void calcularCoordenadas(Ponto ponto, Levantamento levantamento) {
            ponto.setCoordX(levantamento.getCoordX() + ponto.getProjX());
            ponto.setCoordY(levantamento.getCoordY() + ponto.getProjY());
    }

    public Levantamento calcularAreaDeterminantes(Levantamento levantamento) {
        if(!pontoRepository.existsByLevantamentoId(levantamento.getId())){
            throw new LevantamentoNotFoundException("Levantamento não encontrado para o ID: " + levantamento.getId());
        }
        List<Ponto> pontos = pontoRepository.findByLevantamentoId(levantamento.getId());
        double area = 0.0;
        double det1 = 0.0;
        double det2 = 0.0;

        if(pontos.size() < 3){
            throw new IllegalArgumentException("O levantamento precisa de pelo menos 3 pontos para calcular a área.");
        }

        for(int i = 0; i < pontos.size(); i++) {
            if(i == pontos.size() - 1) {
                det1 += pontos.get(i).getCoordY() * pontos.get(0).getCoordX();
                det2 += pontos.get(i).getCoordX() * pontos.get(0).getCoordY();

            }else{
                det1 += pontos.get(i).getCoordY() * pontos.get(i+1).getCoordX();
                det2 += pontos.get(i).getCoordX() * pontos.get(i+1).getCoordY();
            }
        }
        levantamento.setArea(Math.abs((det1 - det2) / 2));

        return levantamento;
    }

    public Levantamento calcularPerimetro(Levantamento levantamento) {
        if(!pontoRepository.existsByLevantamentoId(levantamento.getId())){
            throw new LevantamentoNotFoundException("Levantamento não encontrado para o ID: " + levantamento.getId());
        }

        List<Ponto> pontos = pontoRepository.findByLevantamentoId(levantamento.getId());


        if(pontos.size() < 2){
            throw new IllegalArgumentException("O levantamento precisa de pelo menos 2 pontos para calcular o perímetro.");
        }

        double perimetro = 0.0;

        for(int i = 0; i < pontos.size(); i++) {
            if(i == pontos.size() - 1) {
                double dx = pontos.get(0).getCoordX() - pontos.get(i).getCoordX();
                double dy = pontos.get(0).getCoordY() - pontos.get(i).getCoordY();

                perimetro += Math.sqrt(dx * dx + dy * dy);
            }else{
                double dx = pontos.get(i + 1).getCoordX() - pontos.get(i).getCoordX();
                double dy = pontos.get(i + 1).getCoordY() - pontos.get(i).getCoordY();

                perimetro += Math.sqrt(dx * dx + dy * dy);
            }
        }
        levantamento.setPerimetro(perimetro);
        return levantamento;
    }

    public Levantamento calcularAreaEPerimetro(Levantamento levantamento) {
        calcularAreaDeterminantes(levantamento);
        calcularPerimetro(levantamento);

        return levantamento;
    }

    public Levantamento calcularErroAngular(Levantamento levantamento) {
        int nPontos = pontoRepository.countByLevantamentoId(levantamento.getId());
        double soma = 0.0;

        List<Ponto>  pontos = pontoRepository.findByLevantamentoId(levantamento.getId());
        for (Ponto ponto : pontos) {
            soma += ponto.getAnguloLido();
        }

        double erroAngular = soma - (nPontos - 2) * 180;

        if(erroAngular > 100 || erroAngular < -100){
            throw new IllegalArgumentException("Erro angular muito alto: " + erroAngular);
        }else {
            levantamento.setErroAngular(erroAngular);
        }
        return levantamento;
    }

}
