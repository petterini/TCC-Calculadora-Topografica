package com.pedropetterini.calculadora_topografica.controllers;

import com.pedropetterini.calculadora_topografica.dtos.UsuarioDTO;
import com.pedropetterini.calculadora_topografica.exceptions.UserNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Usuario;
import com.pedropetterini.calculadora_topografica.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;
import java.util.UUID;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrarUsuario")
    public ResponseEntity<Object> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            var usuario = usuarioService.cadastrarUsuario(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/buscarPorId/{idUsuario}")
    public ResponseEntity<Object> buscarUsuarioPorId(@PathVariable UUID idUsuario) {
        try{
            var usuario = usuarioService.buscarPorId(idUsuario);
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        }catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/buscarPorEmail")
    public ResponseEntity<Object> buscarUsuarioPorEmail(@PathVariable String email) {
        try{
            var usuario = usuarioService.buscarPorEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        }catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            var usuario = usuarioService.login(usuarioDTO);
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        }catch (UserNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
