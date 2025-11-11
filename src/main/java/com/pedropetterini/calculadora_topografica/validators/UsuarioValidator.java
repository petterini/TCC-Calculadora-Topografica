package com.pedropetterini.calculadora_topografica.validators;

import com.pedropetterini.calculadora_topografica.dtos.UsuarioDTO;
import com.pedropetterini.calculadora_topografica.exceptions.UserDuplicatedException;
import com.pedropetterini.calculadora_topografica.models.Usuario;
import com.pedropetterini.calculadora_topografica.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioValidator {
    private final UsuarioRepository usuarioRepository;

    public void validate(UsuarioDTO usuario) {
        if(usuarioRepository.existsByEmail(usuario.getEmail()) && usuario.getId() == null){
            throw new UserDuplicatedException("Email já cadastrado.");
        }

        if(usuarioRepository.existsByEmail(usuario.getEmail()) && usuario.getId() != null){
            Usuario user = usuarioRepository.findByEmail(usuario.getEmail()).get();
            if(user.getId() != usuario.getId() && !user.getEmail().equals(usuario.getEmail())){
                throw new UserDuplicatedException("Email já cadastrado.");
            }
        }
    }
}
