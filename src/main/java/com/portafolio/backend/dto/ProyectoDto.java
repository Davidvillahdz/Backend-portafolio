package com.portafolio.backend.dto;

import lombok.Data;

@Data
public class ProyectoDto {
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private String repoUrl;
    private String deployUrl;
    private String tipoParticipacion;
    private String tecnologias;
}