package com.pedropetterini.calculadora_topografica.validators;

import com.pedropetterini.calculadora_topografica.models.Usuario;
import com.pedropetterini.calculadora_topografica.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioValidator {
    private final UsuarioRepository usuarioRepository;

    public void validate(Usuario usuario) {

    }
}
