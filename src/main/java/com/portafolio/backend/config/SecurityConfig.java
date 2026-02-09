package com.portafolio.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // Habilita la config CORS de abajo
                .authorizeHttpRequests(auth -> auth
                        // 1. RUTAS PÚBLICAS (Login y ver perfiles sin loguearse)
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/programadores").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/usuarios/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/proyectos/programador/**").permitAll()
                        // Nota: Usamos "/**" para asegurar que lea cualquier ID después

                        // 2. RUTAS DE LA RÚBRICA (Protegidas)
                        .requestMatchers("/api/asesorias/**").authenticated() // Gestión de Asesorías
                        .requestMatchers("/api/admin/**").authenticated() // Reportes Administrativos
                        .requestMatchers("/api/proyectos/**").authenticated() // Gestión de Proyectos propios

                        // 3. CUALQUIER OTRA COSA
                        .anyRequest().authenticated())

                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Permitir explícitamente el origen de Angular
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        // Permitir TODOS los métodos necesarios para la rúbrica (PUT para responder
        // citas, POST para crear)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}