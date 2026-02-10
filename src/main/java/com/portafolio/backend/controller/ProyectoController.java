package com.portafolio.backend.controller;

import com.portafolio.backend.dto.ProyectoDto;
import com.portafolio.backend.entity.Proyecto;
import com.portafolio.backend.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:4200", "https://portfolio-integrador-31c6f.web.app" })
public class ProyectoController {

    private final ProyectoService proyectoService;

    // MÉTODOS PÚBLICOS

    // Ver proyectos de un experto
    @GetMapping("/programador/{id}")
    public List<Proyecto> proyectosPorProgramador(@PathVariable Long id) {
        return proyectoService.obtenerProyectosPorUsuarioId(id);
    }

    // MÉTODOS PRIVADOS

    // Ver mis propios proyectos
    @GetMapping("/mis-proyectos")
    public List<Proyecto> misProyectos() {
        return proyectoService.obtenerMisProyectos();
    }

    // Crear nuevo proyecto
    @PostMapping
    public Proyecto crearProyecto(@RequestBody ProyectoDto proyectoDto) {
        return proyectoService.crearProyecto(proyectoDto);
    }

    // Borrar proyecto
    @DeleteMapping("/{id}")
    public void borrarProyecto(@PathVariable Long id) {
        proyectoService.borrarProyecto(id);
    }
}