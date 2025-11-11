package com.pedropetterini.calculadora_topografica.controllers;

import com.pedropetterini.calculadora_topografica.dtos.ErroRespostaDTO;
import com.pedropetterini.calculadora_topografica.dtos.LevantamentoDTO;
import com.pedropetterini.calculadora_topografica.dtos.response.LevantamentoResponseDTO;
import com.pedropetterini.calculadora_topografica.exceptions.LevantamentoNotFoundException;
import com.pedropetterini.calculadora_topografica.exceptions.UserNotFoundException;
import com.pedropetterini.calculadora_topografica.services.LevantamentoService;
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
@RequestMapping("levantamentos")
public class LevantamentoController {
    private final LevantamentoService levantamentoService;
    private final PontoService pontoService;

    @PostMapping()
    public ResponseEntity<Object> cadastrarLevantamento(@Valid @RequestBody LevantamentoDTO levantamentoDTO) {
        try {
            var lev = levantamentoService.cadastrar(levantamentoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(lev);
        } catch (UserNotFoundException e){
            var erroDTO = ErroRespostaDTO.usuarioNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("/getLevantamentoById/{id}")
    public ResponseEntity<Object> getLevantamentoById(@PathVariable UUID id) {
        try{
            var levantamento = levantamentoService.getLevantamentoById(id);
            return ResponseEntity.status(HttpStatus.OK).body(levantamento);
        }catch (LevantamentoNotFoundException e){
            var erroDTO = ErroRespostaDTO.levantamentoNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("/getAllLevantamentosByUser/{id}")
    public ResponseEntity<Object> getAllLevantamentosByUser(@PathVariable UUID id) {
        try {
            List<LevantamentoResponseDTO> levantamentos = levantamentoService.getLevantamentoByUser(id);
            return ResponseEntity.ok(levantamentos);
        } catch (LevantamentoNotFoundException e) {
            var erroDTO = ErroRespostaDTO.levantamentoNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("/getAllLevantamentos")
    public ResponseEntity<Object> getAllLevantamentos() {
        try {
            List<LevantamentoResponseDTO> levantamentos = levantamentoService.getAllLevantamentos();
            return ResponseEntity.ok(levantamentos);
        }catch (LevantamentoNotFoundException e){
            var erroDTO = ErroRespostaDTO.levantamentoNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @PostMapping("/calcular/{idLevantamento}")
    public ResponseEntity<Object> calcular(@PathVariable UUID idLevantamento) {
        try{
            LevantamentoResponseDTO response = levantamentoService.calcular(idLevantamento);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (LevantamentoNotFoundException e){
            var erroDTO = ErroRespostaDTO.levantamentoNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @PutMapping
    public ResponseEntity<Object> alterarLevantamento(@RequestBody LevantamentoDTO levantamentoDTO) {
        try{
            var levantamento = levantamentoService.atualizarLevantamento(levantamentoDTO);
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

    @DeleteMapping
    public ResponseEntity<Object> deletarLevantamento(@PathVariable UUID idLevantamento) {
        try {
            pontoService.deletarPorIdLevantamento(idLevantamento);
            levantamentoService.deletarLevantamento(idLevantamento);
            return ResponseEntity.status(HttpStatus.OK).body("Levantamento deletado com sucesso");
        } catch (LevantamentoNotFoundException e){
            var erroDTO = ErroRespostaDTO.levantamentoNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

}
