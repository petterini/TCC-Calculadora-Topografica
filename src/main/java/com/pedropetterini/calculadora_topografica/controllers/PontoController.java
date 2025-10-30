package com.pedropetterini.calculadora_topografica.controllers;

import com.pedropetterini.calculadora_topografica.dtos.PontoDTO;
import com.pedropetterini.calculadora_topografica.dtos.response.PontoResponseDTO;
import com.pedropetterini.calculadora_topografica.exceptions.PontoNotFoundException;
import com.pedropetterini.calculadora_topografica.services.PontoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("ponto")
public class PontoController {
    private final PontoService pontoService;

    @PostMapping
    public ResponseEntity<Object> salvarPonto(@Valid @RequestBody PontoDTO pontoDTO) {
        try {
            var newPonto = this.pontoService.salvarPonto(pontoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPonto);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/cadastrarLista")
    public ResponseEntity<Object> cadastrarLista(@Valid @RequestBody List<PontoDTO> pontos) {
        try {
            List<PontoResponseDTO> pontosDTO = pontoService.salvarListaDePontos(pontos);
            return ResponseEntity.status(HttpStatus.CREATED).body(pontosDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("listarPorLevantamento/{idLevantamento}")
    public ResponseEntity<Object> listarPorLevantamento(@PathVariable UUID idLevantamento) {
        try {
            List<PontoResponseDTO> pontos = pontoService.listarPontosPorLevantamento(idLevantamento);
            return ResponseEntity.status(HttpStatus.OK).body(pontos);
        }catch (PontoNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
