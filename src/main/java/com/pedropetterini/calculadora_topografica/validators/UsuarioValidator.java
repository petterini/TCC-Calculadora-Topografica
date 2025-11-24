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
        if(usuario.getId() == null){
            if(usuarioRepository.existsByEmail(usuario.getEmail())){
                throw new UserDuplicatedException("Email já cadastrado");
            }
        }else{
            if(usuarioRepository.existsByEmailAndIdNot(usuario.getEmail(), usuario.getId()))
                throw new UserDuplicatedException("Email já utilizado por outro usuário");
        }
    }
}
