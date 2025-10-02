package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.models.Usuario;
import com.pedropetterini.calculadora_topografica.repositories.UsuarioRepository;
import com.pedropetterini.calculadora_topografica.validators.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioValidator usuarioValidator;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    public Usuario cadastrarUsuario(Usuario usuario) {
        usuarioValidator.validate(usuario);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }
}
