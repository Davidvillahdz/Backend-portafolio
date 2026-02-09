package com.portafolio.backend.repository;

import com.portafolio.backend.entity.Asesoria;
import com.portafolio.backend.entity.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AsesoriaRepository extends JpaRepository<Asesoria, Long> {

    // Asesorías que tiene que atender un programador
    List<Asesoria> findByProgramador(Usuario programador);

    List<Asesoria> findByCliente(Usuario cliente);
}