package com.portafolio.backend.repository;

import com.portafolio.backend.entity.Usuario;
import com.portafolio.backend.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByRol(Rol rol);

}