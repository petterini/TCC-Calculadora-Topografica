package com.pedropetterini.calculadora_topografica.controllers;

import com.pedropetterini.calculadora_topografica.exceptions.DuplicatedLevantamentoException;
import com.pedropetterini.calculadora_topografica.exceptions.LevantamentoNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Levantamento;
import com.pedropetterini.calculadora_topografica.services.LevantamentoService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LevantamentoController {
    private LevantamentoService levantamentoService;

    @PostMapping("/cadastrarLevantamento")
    public ResponseEntity<Object> cadastrarLevantamento(Levantamento levantamento) {
        try {
            Levantamento lev = levantamentoService.cadastrar(levantamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(lev);
        } catch (DuplicatedLevantamentoException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/getLevantamentoById/{id}")
    public ResponseEntity<Object> getLevantamentoById(@PathVariable UUID id) {
        try{
            Levantamento levantamento = levantamentoService.getLevantamentoById(id);
            return ResponseEntity.status(HttpStatus.OK).body(levantamento);
        }catch (LevantamentoNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/getAllLevantamentosByUser/{id}")
    public ResponseEntity<Object> getAllLevantamentosByUser(@PathVariable UUID id) {
        try {
            List<Levantamento> levantamentos = levantamentoService.getLevantamentoByUser(id);
            return ResponseEntity.ok(levantamentos);
        } catch (LevantamentoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    

}
