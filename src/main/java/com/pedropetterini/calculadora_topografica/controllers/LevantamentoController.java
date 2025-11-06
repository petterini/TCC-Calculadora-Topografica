package com.pedropetterini.calculadora_topografica.controllers;

import com.pedropetterini.calculadora_topografica.dtos.ErroRespostaDTO;
import com.pedropetterini.calculadora_topografica.dtos.LevantamentoDTO;
import com.pedropetterini.calculadora_topografica.dtos.response.LevantamentoResponseDTO;
import com.pedropetterini.calculadora_topografica.exceptions.DuplicatedLevantamentoException;
import com.pedropetterini.calculadora_topografica.exceptions.LevantamentoNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Levantamento;
import com.pedropetterini.calculadora_topografica.services.LevantamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("levantamentos")
public class LevantamentoController {
    private final LevantamentoService levantamentoService;

    @PostMapping()
    public ResponseEntity<Object> cadastrarLevantamento(@Valid @RequestBody LevantamentoDTO levantamentoDTO) {
        try {
            Levantamento lev = levantamentoService.cadastrar(levantamentoDTO);
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
            var erroDTO = ErroRespostaDTO.levantamentoNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("/getAllLevantamentosByUser/{id}")
    public ResponseEntity<Object> getAllLevantamentosByUser(@PathVariable UUID id) {
        try {
            List<Levantamento> levantamentos = levantamentoService.getLevantamentoByUser(id);
            return ResponseEntity.ok(levantamentos);
        } catch (LevantamentoNotFoundException e) {
            var erroDTO = ErroRespostaDTO.levantamentoNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("/getAllLevantamentos")
    public ResponseEntity<Object> getAllLevantamentos() {
        try {
            List<Levantamento> levantamentos = levantamentoService.getAllLevantamentos();
            return ResponseEntity.ok(levantamentos);
        }catch (LevantamentoNotFoundException e){
            var erroDTO = ErroRespostaDTO.levantamentoNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @PostMapping("/calcular/{idLevantamento}")
    public ResponseEntity<Object> calcularArea(@PathVariable UUID idLevantamento) {
        try{
            LevantamentoResponseDTO response = levantamentoService.calcular(idLevantamento);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (LevantamentoNotFoundException e){
            var erroDTO = ErroRespostaDTO.levantamentoNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> alterarLevantamento(@PathVariable UUID id, @Valid @RequestBody LevantamentoDTO levantamentoDTO) {
        try{
            var levantamento = levantamentoService.alterarLevantamento(id, levantamentoDTO);
            return ResponseEntity.status(HttpStatus.OK).body(levantamento);
        }catch (LevantamentoNotFoundException e){
            var erroDTO = ErroRespostaDTO.levantamentoNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @PostMapping("/calcularParcial/{idLevantamento}")
    public ResponseEntity<Object> calcularParcial(@PathVariable UUID idLevantamento, @Valid @RequestBody List<String> lista) {
        try {
            var levantamento = levantamentoService.calcular(idLevantamento, lista);
            return ResponseEntity.status(HttpStatus.OK).body(levantamento);
        }catch (LevantamentoNotFoundException e){
            var errorDTO = ErroRespostaDTO.levantamentoNotFound(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);
        }
    }

}
