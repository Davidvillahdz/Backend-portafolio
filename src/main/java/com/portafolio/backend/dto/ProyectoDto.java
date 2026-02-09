package com.portafolio.backend.dto;

import lombok.Data;

@Data
public class ProyectoDto {
    private String nombre; // Esto se guardará como 'titulo'
    private String descripcion;
    private String imagenUrl;
    private String repoUrl;
    private String deployUrl; // Esto se guardará como 'demoUrl'

    // Estos campos ya no los usamos en la entidad simple, pero los dejamos para que
    // no rompa el DTO
    private String tipoParticipacion;
    private String tecnologias;
}