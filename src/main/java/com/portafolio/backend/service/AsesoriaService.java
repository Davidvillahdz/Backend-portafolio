package com.portafolio.backend.service;

import com.portafolio.backend.entity.Asesoria;
import com.portafolio.backend.entity.EstadoAsesoria;
import com.portafolio.backend.entity.Usuario;
import com.portafolio.backend.repository.AsesoriaRepository;
import com.portafolio.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsesoriaService {

    private final AsesoriaRepository asesoriaRepository;
    private final UsuarioRepository usuarioRepository;

    // MÉTODO 1: Crear una nueva cita
    public Asesoria crearAsesoria(Asesoria asesoria, String emailCliente) {
        // 1. Buscamos al cliente por su email
        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // 2. CORRECCIÓN: Usamos setCliente en lugar de setUsuario
        asesoria.setCliente(cliente);

        // 3. Estado inicial
        asesoria.setEstado(EstadoAsesoria.PENDIENTE);

        // 4. CORRECCIÓN DE FECHA:
        // Si la fecha viniera nula, ponemos una por defecto para que no falle
        if (asesoria.getFecha() == null) {
            asesoria.setFecha(LocalDateTime.now().plusDays(1));
        }

        return asesoriaRepository.save(asesoria);
    }

    // MÉTODO 2: Listar citas de un usuario (sea Cliente o Programador)
    public List<Asesoria> obtenerCitasPorUsuario(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Filtramos las citas donde el usuario sea Cliente O Programador
        List<Asesoria> todas = asesoriaRepository.findAll();

        return todas.stream()
                .filter(a -> (a.getCliente() != null && a.getCliente().getId().equals(usuario.getId())) ||
                        (a.getProgramador() != null && a.getProgramador().getId().equals(usuario.getId())))
                .collect(Collectors.toList());
    }

    // MÉTODO 3: Responder Cita
    public Asesoria responderCita(Long id, String estado, String emailProgramador) {
        // 1. Buscamos la cita
        Asesoria cita = asesoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // 2. SEGURIDAD: Verificamos que el usuario logueado sea el programador de esta
        if (!cita.getProgramador().getEmail().equals(emailProgramador)) {
            throw new RuntimeException("No tienes permiso para gestionar esta cita.");
        }

        // 3. Convertimos el String
        try {
            EstadoAsesoria nuevoEstado = EstadoAsesoria.valueOf(estado);
            cita.setEstado(nuevoEstado);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + estado);
        }

        return asesoriaRepository.save(cita);
    }
}