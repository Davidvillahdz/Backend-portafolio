package com.portafolio.backend.repository;

import com.portafolio.backend.entity.Usuario;
import com.portafolio.backend.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Método mágico para buscar por email (indispensable para el Login)
    Optional<Usuario> findByEmail(String email);

    // Para listar solo programadores en la página de inicio
    List<Usuario> findByRol(Rol rol);

}