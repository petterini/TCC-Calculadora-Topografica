package com.pedropetterini.calculadora_topografica.services;

import com.pedropetterini.calculadora_topografica.dtos.UsuarioDTO;
import com.pedropetterini.calculadora_topografica.dtos.mapper.UsuarioMapper;
import com.pedropetterini.calculadora_topografica.dtos.response.UsuarioResponseDTO;
import com.pedropetterini.calculadora_topografica.exceptions.UserNotFoundException;
import com.pedropetterini.calculadora_topografica.models.Usuario;
import com.pedropetterini.calculadora_topografica.repositories.UsuarioRepository;
import com.pedropetterini.calculadora_topografica.validators.UsuarioValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioValidator usuarioValidator;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioResponseDTO cadastrarUsuario(UsuarioDTO usuarioDTO) {
        usuarioValidator.validate(usuarioDTO);

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));

        usuario = usuarioRepository.save(usuario);

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(usuario.getId());
        usuarioResponseDTO.setEmail(usuario.getEmail());
        usuarioResponseDTO.setNome(usuario.getNome());

        return usuarioResponseDTO;
    }

    public Usuario buscarPorId(UUID idUsuario) {
        return usuarioRepository.findById(idUsuario).orElseThrow(() -> new UserNotFoundException("Usuário não encontrado para o id: " + idUsuario));
    }

    public UsuarioResponseDTO login(UsuarioDTO usuarioDTO) {
        String email = usuarioDTO.getEmail();
        String senha = usuarioDTO.getSenha();

        Usuario user = usuarioRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Email ou senha incorretos."));

        if (!passwordEncoder.matches(senha, user.getSenha())) {
            throw new UserNotFoundException("Email ou senha incorretos.");
        }

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(user.getId());
        usuarioResponseDTO.setEmail(user.getEmail());
        usuarioResponseDTO.setNome(user.getNome());

        return usuarioResponseDTO;
    }

    public UsuarioResponseDTO buscarPorEmail(String email) {
        Usuario user = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("Usuario não encontrado para o email informado."));

        UsuarioResponseDTO userDTO = new UsuarioResponseDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setNome(user.getNome());

        return userDTO;
    }


}
