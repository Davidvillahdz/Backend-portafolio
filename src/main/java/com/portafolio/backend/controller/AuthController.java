package com.portafolio.backend.controller;

import com.portafolio.backend.dto.AuthResponse;
import com.portafolio.backend.dto.LoginRequest;
import com.portafolio.backend.dto.RegisterRequest;
import com.portafolio.backend.repository.UsuarioRepository;
import com.portafolio.backend.service.JwtService;
import com.portafolio.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        String token = usuarioService.registrarUsuario(request);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails user = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}