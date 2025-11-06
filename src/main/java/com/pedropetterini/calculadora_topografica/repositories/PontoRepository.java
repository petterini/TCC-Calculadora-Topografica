package com.pedropetterini.calculadora_topografica.repositories;

import com.pedropetterini.calculadora_topografica.models.Ponto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PontoRepository extends JpaRepository<Ponto, UUID> {
    List<Ponto> findByLevantamentoId(UUID idLevantamento);

    boolean existsByLevantamentoId(UUID idLevantamento);

    Optional<Ponto> findByNomeAndLevantamentoId(@NotNull String nome, @NotNull UUID idLevantamento);

    @Query("SELECT p FROM Ponto p WHERE p.levantamento.id = :levantamentoId AND p.nome LIKE 'Estacao%'")
    List<Ponto> findEstacoesByLevantamentoId(@Param("levantamentoId") UUID levantamentoId);

    @Query("SELECT p FROM Ponto p WHERE p.levantamento.id = :levantamentoId AND p.nome LIKE 'Ponto%'")
    List<Ponto> findPontosByLevantamentoId(@Param("levantamentoId") UUID levantamentoId);

}
