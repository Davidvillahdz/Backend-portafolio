package com.portafolio.backend.controller;

import com.portafolio.backend.dto.AsesoriaRequest;
import com.portafolio.backend.entity.Asesoria;
import com.portafolio.backend.entity.EstadoAsesoria;
import com.portafolio.backend.entity.Usuario;
import com.portafolio.backend.repository.AsesoriaRepository;
import com.portafolio.backend.repository.UsuarioRepository;
import com.portafolio.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/asesorias")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:4200", "https://portfolio-integrador-31c6f.web.app" })
public class AsesoriaController {

    private final AsesoriaRepository asesoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    @PostMapping
    public Asesoria solicitarAsesoria(@RequestBody AsesoriaRequest request) {
        String emailCliente = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Usuario programador = usuarioRepository.findById(request.getProgramadorId())
                .orElseThrow(() -> new RuntimeException("Programador no encontrado"));

        Asesoria cita = new Asesoria();
        cita.setTema(request.getTema());
        cita.setCelular(request.getCelular());
        cita.setCliente(cliente);
        cita.setProgramador(programador);
        cita.setEstado(EstadoAsesoria.PENDIENTE);

        // Formateo de fecha
        String fechaString = request.getFecha();
        if (fechaString.length() == 16) {
            fechaString += ":00";
        }
        cita.setFecha(LocalDateTime.parse(fechaString));

        Asesoria guardada = asesoriaRepository.save(cita);

        // 📧 El envío ahora es asíncrono, no retrasará la respuesta del servidor
        emailService.enviarCorreo(
                programador.getEmail(),
                "🚀 Nueva Solicitud de Asesoría",
                "Hola " + programador.getNombre() + ",\n\n" +
                        "Tienes una nueva solicitud de " + cliente.getNombre() + ".\n" +
                        "Tema: " + request.getTema() + "\n" +
                        "Fecha: " + fechaString + "\n\n" +
                        "Revisa tu Dashboard para responder.");

        return guardada;
    }

    @GetMapping("/mis-pedidos")
    public List<Asesoria> misPedidos() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario cliente = usuarioRepository.findByEmail(email).orElseThrow();
        return asesoriaRepository.findByCliente(cliente);
    }

    @GetMapping("/recibidas")
    public List<Asesoria> citasRecibidas() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario prog = usuarioRepository.findByEmail(email).orElseThrow();
        return asesoriaRepository.findByProgramador(prog);
    }

    @PutMapping("/{id}/responder")
    public Asesoria responder(@PathVariable Long id, @RequestParam String estado) {
        Asesoria cita = asesoriaRepository.findById(id).orElseThrow();
        cita.setEstado(EstadoAsesoria.valueOf(estado));
        Asesoria citaActualizada = asesoriaRepository.save(cita);

        String asunto = "Actualización de tu Asesoría: " + estado;
        String mensaje = "Hola " + cita.getCliente().getNombre() + ",\n\n" +
                "Tu solicitud sobre '" + cita.getTema() + "' ha sido " + estado + ".\n" +
                "Por: " + cita.getProgramador().getNombre();

        if (estado.equals("ACEPTADA")) {
            mensaje += "\n\nPronto se pondrán en contacto contigo vía WhatsApp.";
        }

        emailService.enviarCorreo(cita.getCliente().getEmail(), asunto, mensaje);

        return citaActualizada;
    }
}