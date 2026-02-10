package com.portafolio.backend.service;

import com.portafolio.backend.dto.RegisterRequest;
import com.portafolio.backend.entity.Rol;
import com.portafolio.backend.entity.Usuario;
import com.portafolio.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String registrarUsuario(RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());

        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        usuario.setRol(request.getRol() != null ? request.getRol() : Rol.USUARIO);

        usuario.setEspecialidad(request.getEspecialidad());
        usuario.setDescripcion(request.getDescripcion());
        usuario.setGithub(request.getGithub());
        usuario.setLinkedin(request.getLinkedin());
        usuario.setWhatsapp(request.getWhatsapp());

        usuarioRepository.save(usuario);

        return jwtService.generateToken(usuario);
    }
}