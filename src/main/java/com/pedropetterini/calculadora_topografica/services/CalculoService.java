package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.exceptions.LevantamentoNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Levantamento;
import com.pedropetterini.calculadora_topografica.models.Ponto;
import com.pedropetterini.calculadora_topografica.repositories.PontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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

    public void calcularAreaDeterminantes(Levantamento levantamento) {
        if (!pontoRepository.existsByLevantamentoId(levantamento.getId())) {
            throw new LevantamentoNotFoundException("Levantamento não encontrado para o ID: " + levantamento.getId());
        }
        List<Ponto> pontos = pontoRepository.findByLevantamentoId(levantamento.getId());
        double det1 = 0.0;
        double det2 = 0.0;

        if (pontos.size() < 3) {
            throw new IllegalArgumentException("O levantamento precisa de pelo menos 3 pontos para calcular a área.");
        }

        for (int i = 0; i < pontos.size(); i++) {
            if (i == pontos.size() - 1) {
                det1 += pontos.get(i).getCoordY() * pontos.get(0).getCoordX();
                det2 += pontos.get(i).getCoordX() * pontos.get(0).getCoordY();

            } else {
                det1 += pontos.get(i).getCoordY() * pontos.get(i + 1).getCoordX();
                det2 += pontos.get(i).getCoordX() * pontos.get(i + 1).getCoordY();
            }
        }
        levantamento.setArea(Math.abs((det1 - det2) / 2));

    }

    public Levantamento calcularAreaEPerimetroPorListaDePontos(Levantamento levantamento, List<String> nomes) {

        Levantamento lev = levantamento;
        lev.setNome("Cálculo parcial do levantamento: " + levantamento.getNome());

        List<Ponto> pontos = pontoRepository.findAllByNomesAndLevantamento(nomes, levantamento.getId());
        pontos.sort(Comparator.comparing(p -> nomes.indexOf(p.getNome())));


        double det1 = 0.0;
        double det2 = 0.0;

        double perimetro = 0;
        double dx = 0;
        double dy = 0;

        if (pontos.size() < 3) {
            throw new IllegalArgumentException("O levantamento precisa de pelo menos 3 pontos para calcular a área.");
        }

        for (int i = 0; i < pontos.size(); i++) {
            if (i == pontos.size() - 1) {
                det1 += pontos.get(i).getCoordY() * pontos.get(0).getCoordX();
                det2 += pontos.get(i).getCoordX() * pontos.get(0).getCoordY();

                dx = pontos.get(0).getCoordX() - pontos.get(i).getCoordX();
                dy = pontos.get(0).getCoordY() - pontos.get(i).getCoordY();

            } else {
                det1 += pontos.get(i).getCoordY() * pontos.get(i + 1).getCoordX();
                det2 += pontos.get(i).getCoordX() * pontos.get(i + 1).getCoordY();

                dx = pontos.get(i + 1).getCoordX() - pontos.get(i).getCoordX();
                dy = pontos.get(i + 1).getCoordY() - pontos.get(i).getCoordY();
            }
            perimetro += Math.sqrt(dx * dx + dy * dy);
        }
        lev.setArea(Math.abs((det1 - det2) / 2));


        return lev;

    }

    public void calcularPerimetro(Levantamento levantamento) {
        if (!pontoRepository.existsByLevantamentoId(levantamento.getId())) {
            throw new LevantamentoNotFoundException("Levantamento não encontrado para o ID: " + levantamento.getId());
        }

        List<Ponto> pontos = pontoRepository.findByLevantamentoId(levantamento.getId());


        if (pontos.size() < 2) {
            throw new IllegalArgumentException("O levantamento precisa de pelo menos 2 pontos para calcular o perímetro.");
        }

        double perimetro = 0.0;

        for (int i = 0; i < pontos.size(); i++) {
            if (i == pontos.size() - 1) {
                double dx = pontos.get(0).getCoordX() - pontos.get(i).getCoordX();
                double dy = pontos.get(0).getCoordY() - pontos.get(i).getCoordY();

                perimetro += Math.sqrt(dx * dx + dy * dy);
            } else {
                double dx = pontos.get(i + 1).getCoordX() - pontos.get(i).getCoordX();
                double dy = pontos.get(i + 1).getCoordY() - pontos.get(i).getCoordY();

                perimetro += Math.sqrt(dx * dx + dy * dy);
            }
        }
        levantamento.setPerimetro(perimetro);
    }

    public void calcularAreaEPerimetro(Levantamento levantamento) {
        calcularAreaDeterminantes(levantamento);
        calcularPerimetro(levantamento);

    }

    public void calcularErroAngular(Levantamento levantamento) {
        int nPontos = 0;
        List<Ponto> pontos = new ArrayList<>();

        if (levantamento.getTipo().equals("Caminhamento")) {
            pontos = pontoRepository.findByLevantamentoId(levantamento.getId());
            nPontos = pontos.size();
        } else if (levantamento.getTipo().equals("Caminhamento Irradiado")) {
            pontos = pontoRepository.findEstacoesByLevantamentoId(levantamento.getId());
            nPontos = pontos.size();
        }


        double soma = 0.0;

        for (Ponto ponto : pontos) {
            soma += ponto.getAnguloLido();
        }

        double erroAngular = soma - ((nPontos - 2) * 180);

        levantamento.setErroAngular(erroAngular);

    }

    public void calcularCaminhamento(Levantamento levantamento) {

        List<Ponto> pontos = new ArrayList<>();

        if (levantamento.getTipo().equals("Caminhamento")) {
            pontos = pontoRepository.findByLevantamentoId(levantamento.getId());
        } else if (levantamento.getTipo().equals("Caminhamento Irradiado")) {
            pontos = pontoRepository.findEstacoesByLevantamentoId(levantamento.getId());
        }
        double correcao = -levantamento.getErroAngular() / pontos.size();
        double perimetro = 0.0;
        double projX = 0;
        double projY = 0;
        for (Ponto ponto : pontos) {
            ponto.setAnguloHz(ponto.getAnguloLido() + correcao);
            if (ponto.getReferencia() != null) {
                double az = ponto.getReferencia().getAzimute() + ponto.getAnguloHz() + 180;

                if (az > 360)
                    az -= 360;

                ponto.setAzimute(az);

            } else if (levantamento.getTipo().equals("Caminhamento Irradiado") && ponto.getAzimuteRe() > 0) {
                double az = ponto.getAzimuteRe() + ponto.getAnguloHz();

                if (az > 360)
                    az -= 360;

                ponto.setAzimute(az);
            }

            calcularProjecoes(ponto);

            perimetro += ponto.getDistancia();
            projX += ponto.getProjX();
            projY += ponto.getProjY();
        }

        levantamento.setErroLinearAbs(Math.sqrt(projX * projX + projY * projY));
        levantamento.setPerimetro(perimetro);
        calcularCoordenadas(pontos, levantamento, projX, projY);
    }

    public void calcularCoordenadas(List<Ponto> pontos, Levantamento levantamento, double projX, double projY) {
        double compensacaoX = projX / levantamento.getPerimetro();
        double compensacaoY = projY / levantamento.getPerimetro();

        for (Ponto ponto : pontos) {
            ponto.setCProjX(ponto.getDistancia() * compensacaoX);
            ponto.setCProjY(ponto.getDistancia() * compensacaoY);

            if (ponto.getReferencia() == null) {
                ponto.setCoordX(ponto.getProjX() + -ponto.getCProjX() + levantamento.getCoordX());
                ponto.setCoordY(ponto.getProjY() + -ponto.getCProjY() + levantamento.getCoordY());
            } else {
                ponto.setCoordX(ponto.getProjX() + -ponto.getCProjX() + ponto.getReferencia().getCoordX());
                ponto.setCoordY(ponto.getProjY() + -ponto.getCProjY() + ponto.getReferencia().getCoordY());
            }
        }

        for (int i = 0; i < pontos.size(); i++) {
            if (i == pontos.size() - 1) {
                pontos.get(i).setYx(pontos.get(i).getCoordY() * pontos.get(0).getCoordX());
                pontos.get(i).setXy(pontos.get(i).getCoordX() * pontos.get(0).getCoordY());
            } else {
                pontos.get(i).setYx(pontos.get(i).getCoordY() * pontos.get(i + 1).getCoordX());
                pontos.get(i).setXy(pontos.get(i).getCoordX() * pontos.get(i + 1).getCoordY());
            }
        }

    }

    public void calcularArea(Levantamento levantamento) {
        List<Ponto> pontos = pontoRepository.findByLevantamentoId(levantamento.getId());
        double somaXY = 0;
        double somaYX = 0;

        for (Ponto ponto : pontos) {
            somaXY += ponto.getXy();
            somaYX += ponto.getYx();
        }

        levantamento.setArea((somaXY - somaYX) / 2);
    }

    public void calcularPontosIrradiados(Levantamento levantamento) {
        List<Ponto> pontos = pontoRepository.findPontosByLevantamentoId(levantamento.getId());
        for (Ponto ponto : pontos) {
            ponto.setAnguloHz(ponto.getAnguloLido());
            double azimute = ponto.getReferencia().getAzimute() + ponto.getAnguloHz() + 180;
            System.out.println("Ponto: " + ponto.getNome() + " Azimute: " + azimute);
            while (azimute > 360) {
                azimute -= 360;
            }
            System.out.println("Ponto: " + ponto.getNome() + " Azimute: " + azimute);
            ponto.setAzimute(azimute);
            calcularProjecoes(ponto);
            ponto.setCoordX(ponto.getProjX() + ponto.getReferencia().getCoordX());
            ponto.setCoordY(ponto.getProjY() + ponto.getReferencia().getCoordY());
        }
    }


}
