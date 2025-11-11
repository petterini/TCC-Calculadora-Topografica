package com.pedropetterini.calculadora_topografica.repositories;

import com.pedropetterini.calculadora_topografica.models.Levantamento;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LevantamentoRepository extends JpaRepository<Levantamento, UUID> {
    List<Levantamento> findLevantamentoByUsuarioId(@NotNull UUID idUsuario);

    Levantamento getLevantamentoById(UUID id);
}
