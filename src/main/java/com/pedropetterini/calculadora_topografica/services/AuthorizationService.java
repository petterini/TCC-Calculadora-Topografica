package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDetails user = usuarioRepository.findByEmail(username);
        if(user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        return user;
    }
}
