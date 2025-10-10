package com.pedropetterini.calculadora_topografica.repositories;

import com.pedropetterini.calculadora_topografica.models.Levantamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LevantamentoRepository extends JpaRepository<Levantamento, UUID> {
}
