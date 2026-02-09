package com.portafolio.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore; // Importante
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private String especialidad;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private String fotoPerfil;
    @Column(name = "horario_disponibilidad")
    private String horario; // Ej: "Lunes a Viernes: 9am - 12pm"
    @Column(name = "modalidad")
    private String modalidad; // Ej: "VIRTUAL", "PRESENCIAL", "HIBRIDO"

    // Redes Sociales
    private String github;
    private String linkedin;
    private String whatsapp;

    // --- AQUÍ ESTÁ EL ARREGLO MÁGICO ---
    // Agregamos las listas con @JsonIgnore para romper el bucle infinito

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("usuario") // Evitamos bucle infinito
    private List<Proyecto> proyectos;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Asesoria> asesoriasPedidas;

    @OneToMany(mappedBy = "programador", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Asesoria> asesoriasRecibidas;
    // ------------------------------------

    // --- MÉTODOS DE SEGURIDAD (UserDetails) ---
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (rol == null)
            return List.of();
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

}