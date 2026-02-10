package com.portafolio.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@Table(name = "horarios_disponibilidad")
public class HorarioDisponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    @ManyToOne
    @JoinColumn(name = "programador_id", nullable = false)
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Usuario programador;
}