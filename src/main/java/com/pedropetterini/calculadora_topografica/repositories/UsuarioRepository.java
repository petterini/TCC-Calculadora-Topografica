package com.pedropetterini.calculadora_topografica.repositories;

import com.pedropetterini.calculadora_topografica.models.Usuario;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    boolean existsByEmail(String email);

    UserDetails findByEmail(String email);


    Usuario getByEmail(@NotBlank String email);

    boolean existsByEmailAndIdNot(@NotBlank String email, UUID id);
}
