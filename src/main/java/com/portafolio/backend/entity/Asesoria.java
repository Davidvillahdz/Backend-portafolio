package com.portafolio.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "asesorias")
public class Asesoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tema;
    private String celular;
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private EstadoAsesoria estado;
    @ManyToOne
    @JoinColumn(name = "programador_id")
    private Usuario programador;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Usuario cliente;
}