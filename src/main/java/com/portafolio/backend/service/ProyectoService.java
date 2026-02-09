package com.portafolio.backend.service;

import com.portafolio.backend.dto.ProyectoDto;
import com.portafolio.backend.entity.Proyecto;
import com.portafolio.backend.entity.Usuario;
import com.portafolio.backend.repository.ProyectoRepository;
import com.portafolio.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;

    // 1. CREAR PROYECTO (Privado)
    public Proyecto crearProyecto(ProyectoDto dto) {
        Usuario usuario = obtenerUsuarioActual();

        Proyecto proyecto = new Proyecto();
        proyecto.setTitulo(dto.getNombre());
        proyecto.setDescripcion(dto.getDescripcion());
        proyecto.setImagenUrl(dto.getImagenUrl());
        proyecto.setRepoUrl(dto.getRepoUrl());
        proyecto.setDemoUrl(dto.getDeployUrl());
        proyecto.setUsuario(usuario);

        return proyectoRepository.save(proyecto);
    }

    // 2. LISTAR MIS PROYECTOS (Privado - Para el panel de control)
    public List<Proyecto> obtenerMisProyectos() {
        Usuario usuario = obtenerUsuarioActual();
        return proyectoRepository.findByUsuario(usuario);
    }

    // 3. LISTAR PROYECTOS DE UN EXPERTO (Público - Por ID)
    // Este es el método que te faltaba y causaba el error
    public List<Proyecto> obtenerProyectosPorUsuarioId(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return proyectoRepository.findByUsuario(usuario);
    }

    // 4. BORRAR PROYECTO (Privado)
    public void borrarProyecto(Long id) {
        Usuario usuario = obtenerUsuarioActual();
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        if (!proyecto.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permiso para borrar este proyecto");
        }
        proyectoRepository.deleteById(id);
    }

    // Auxiliar para saber quién está logueado
    private Usuario obtenerUsuarioActual() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}