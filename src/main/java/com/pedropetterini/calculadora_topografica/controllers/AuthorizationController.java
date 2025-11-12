package com.pedropetterini.calculadora_topografica.controllers;

import com.pedropetterini.calculadora_topografica.configs.TokenService;
import com.pedropetterini.calculadora_topografica.dtos.AuthenticationDTO;
import com.pedropetterini.calculadora_topografica.dtos.CadastroDTO;
import com.pedropetterini.calculadora_topografica.dtos.LoginResponseDTO;
import com.pedropetterini.calculadora_topografica.exceptions.UserDuplicatedException;
import com.pedropetterini.calculadora_topografica.models.Usuario;
import com.pedropetterini.calculadora_topografica.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthenticationDTO data){
         var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
         var auth = this.authenticationManager.authenticate(usernamePassword);

         var token = tokenService.generateToken((Usuario) auth.getPrincipal());

         return  ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrar(@RequestBody @Valid CadastroDTO data){
        if(usuarioRepository.existsByEmail(data.email())){
            throw new UserDuplicatedException("Usuario já cadastrado");
        }

        String senhaCriptografada = new BCryptPasswordEncoder().encode(data.senha());
        Usuario usuario = new Usuario(data.nome(), data.email(), senhaCriptografada);

        usuarioRepository.save(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
