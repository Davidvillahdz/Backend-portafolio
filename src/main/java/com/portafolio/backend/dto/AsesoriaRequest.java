package com.portafolio.backend.dto;

import lombok.Data;

@Data
public class AsesoriaRequest {
    private Long programadorId;
    private String tema;
    private String celular;
    private String fecha;
}