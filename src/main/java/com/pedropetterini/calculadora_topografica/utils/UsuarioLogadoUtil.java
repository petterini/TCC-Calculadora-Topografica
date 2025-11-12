package com.pedropetterini.calculadora_topografica.utils;

import com.pedropetterini.calculadora_topografica.configs.TokenService;
import com.pedropetterini.calculadora_topografica.exceptions.UserNotLoggedException;
import com.pedropetterini.calculadora_topografica.models.Usuario;
import com.pedropetterini.calculadora_topografica.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class UsuarioLogadoUtil {
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    public Usuario getUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.getPrincipal() instanceof Usuario){
            return (Usuario) authentication.getPrincipal();
        }
        throw new UserNotLoggedException("Nenhum usuário logado.");
    }

}
