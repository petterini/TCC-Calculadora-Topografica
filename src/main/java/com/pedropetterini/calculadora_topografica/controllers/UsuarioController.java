package com.pedropetterini.calculadora_topografica.controllers;

import com.pedropetterini.calculadora_topografica.dtos.erro.ErroRespostaDTO;
import com.pedropetterini.calculadora_topografica.dtos.UsuarioDTO;
import com.pedropetterini.calculadora_topografica.exceptions.UserDuplicatedException;
import com.pedropetterini.calculadora_topografica.exceptions.UserNotFoundException;
import com.pedropetterini.calculadora_topografica.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/cadastrarUsuario")
    public ResponseEntity<Object> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            var usuario = usuarioService.cadastrarUsuario(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        }catch (UserDuplicatedException e){
            var erroDTO = ErroRespostaDTO.usuarioDuplicado(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(erroDTO);
        }
    }

    @GetMapping("/buscarPorId/{idUsuario}")
    public ResponseEntity<Object> buscarUsuarioPorId(@PathVariable UUID idUsuario) {
        try{
            var usuario = usuarioService.buscarPorId(idUsuario);
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        }catch (UserNotFoundException e){
            var erroDTO = ErroRespostaDTO.usuarioNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<Object> atualizarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            var usuario = usuarioService.atualizarUsuario(usuarioDTO);
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        }catch (UserNotFoundException e){
            var erroDTO = ErroRespostaDTO.usuarioNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(e.getMessage());
        }catch (UserDuplicatedException e){
            var errorDTO = ErroRespostaDTO.usuarioDuplicado(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deletarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            var usuario = usuarioService.deletarUsuario(usuarioDTO);
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        } catch (UserNotFoundException e){
            var erroDTO = ErroRespostaDTO.usuarioNotFound(e.getMessage());
            return ResponseEntity.status(erroDTO.status()).body(e.getMessage());
        }
    }
}
