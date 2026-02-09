package com.portafolio.backend.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.portafolio.backend.entity.Usuario;
import com.portafolio.backend.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final UsuarioRepository usuarioRepository;

    @GetMapping("/usuarios/pdf")
    public void exportarUsuariosPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_usuarios.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("REPORTE DE USUARIOS - DEVPORTFOLIO").setBold().setFontSize(18));
        document.add(new Paragraph("Generado por: Doctor Cartman"));

        Table table = new Table(4);
        table.addCell("ID");
        table.addCell("Nombre");
        table.addCell("Email");
        table.addCell("Rol");

        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            table.addCell(u.getId().toString());
            table.addCell(u.getNombre());
            table.addCell(u.getEmail());
            table.addCell(u.getRol().toString());
        }

        document.add(table);
        document.close();
    }
}