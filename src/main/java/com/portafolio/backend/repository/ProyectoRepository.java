package com.portafolio.backend.repository;

import com.portafolio.backend.entity.Proyecto;
import com.portafolio.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    // ESTE ES EL MÉTODO QUE JAVA TE ESTÁ PIDIENDO A GRITOS 👇
    List<Proyecto> findByUsuario(Usuario usuario);

}