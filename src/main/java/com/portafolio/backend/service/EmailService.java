package com.portafolio.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String remitente;

    @Async
    public void enviarCorreo(String destinatario, String asunto, String mensaje) {
        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(remitente);
            email.setTo(destinatario);
            email.setSubject(asunto);
            email.setText(mensaje);

            mailSender.send(email);
            System.out.println("📧 Correo enviado a: " + destinatario);
        } catch (Exception e) {
            System.err.println("❌ Error enviando correo: " + e.getMessage());
        }
    }
}