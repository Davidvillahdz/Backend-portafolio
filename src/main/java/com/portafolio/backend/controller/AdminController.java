package com.portafolio.backend.controller;

import com.portafolio.backend.entity.Rol;
import com.portafolio.backend.entity.Usuario;
import com.portafolio.backend.repository.AsesoriaRepository;
import com.portafolio.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final AsesoriaRepository asesoriaRepository;

    // 1. ESTADÍSTICAS (Dashboard)
    @GetMapping("/stats")
    public Map<String, Long> obtenerEstadisticas() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalUsuarios", usuarioRepository.count());
        stats.put("totalCitas", asesoriaRepository.count());
        long programadores = usuarioRepository.findAll().stream()
                .filter(u -> "PROGRAMADOR".equals(u.getRol().toString()))
                .count();
        stats.put("totalProgramadores", programadores);

        return stats;
    }

    // 2. LISTAR TODOS LOS USUARIOS (Tabla)
    @GetMapping("/users")
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // 3. ELIMINAR USUARIO
    @DeleteMapping("/users/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
    }

    // 4. CAMBIAR ROL (Ascender/Descender)
    @PutMapping("/users/{id}/rol")
    public Usuario cambiarRol(@PathVariable Long id, @RequestParam String nuevoRol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setRol(Rol.valueOf(nuevoRol));

        return usuarioRepository.save(usuario);
    }
}