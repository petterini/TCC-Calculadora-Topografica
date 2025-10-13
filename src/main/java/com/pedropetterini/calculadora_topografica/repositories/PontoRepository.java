package com.pedropetterini.calculadora_topografica.repositories;

import com.pedropetterini.calculadora_topografica.models.Ponto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PontoRepository extends JpaRepository<Ponto, UUID> {
}
