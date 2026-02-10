package com.portafolio.backend.repository;

import com.portafolio.backend.entity.Proyecto;
import com.portafolio.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByUsuario(Usuario usuario);

}