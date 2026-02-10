package com.portafolio.backend.dto;

import lombok.Data;
import com.portafolio.backend.entity.Rol;

@Data
public class RegisterRequest {
    private String nombre;
    private String email;
    private String password;
    private Rol rol;
    private String especialidad;
    private String descripcion;
    private String github;
    private String linkedin;
    private String whatsapp;
}