package com.portafolio.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "proyectos")
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    private String imagenUrl;
    private String repoUrl;
    private String demoUrl;

    // --- AQUÍ ESTÁ LA CLAVE ---
    // Aunque lógicamente es un "Programador", la tabla en BD se llama "usuario"
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id") // Esto crea la columna que guarda el ID del programador
    @JsonIgnoreProperties({ "password", "roles", "proyectos", "tokens" })
    private Usuario usuario;
}