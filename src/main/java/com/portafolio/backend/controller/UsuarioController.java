package com.portafolio.backend.controller;

import com.portafolio.backend.entity.Usuario;
import com.portafolio.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    // 1. LISTA DE PROGRAMADORES (Público - Para el Home)
    @GetMapping("/programadores")
    public List<Usuario> listarProgramadores() {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getRol().toString().equals("PROGRAMADOR"))
                .collect(Collectors.toList());
    }

    // 2. OBTENER PERFIL PÚBLICO POR ID (Público - Para ver detalle del experto)
    @GetMapping("/{id}")
    public Usuario obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // 3. MI PERFIL (Privado)
    @GetMapping("/me")
    public Usuario miPerfil() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // 4. ACTUALIZAR MI PERFIL (Privado)
    @PutMapping("/me")
    public Usuario actualizarPerfil(@RequestBody Usuario nuevosDatos) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Campos existentes...
        usuarioActual.setNombre(nuevosDatos.getNombre());
        usuarioActual.setEspecialidad(nuevosDatos.getEspecialidad());
        usuarioActual.setDescripcion(nuevosDatos.getDescripcion());
        usuarioActual.setGithub(nuevosDatos.getGithub());
        usuarioActual.setLinkedin(nuevosDatos.getLinkedin());
        usuarioActual.setWhatsapp(nuevosDatos.getWhatsapp());

        // 🔥 ESTO ES LO QUE FALTA (AGRÉGALO):
        usuarioActual.setHorario(nuevosDatos.getHorario()); // <--- IMPORTANTE
        usuarioActual.setModalidad(nuevosDatos.getModalidad()); // <--- IMPORTANTE

        if (nuevosDatos.getFotoPerfil() != null && !nuevosDatos.getFotoPerfil().isEmpty()) {
            usuarioActual.setFotoPerfil(nuevosDatos.getFotoPerfil());
        }

        return usuarioRepository.save(usuarioActual);
    }
}