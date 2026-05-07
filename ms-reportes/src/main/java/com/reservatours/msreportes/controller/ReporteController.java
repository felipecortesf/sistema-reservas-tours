package com.reservatours.msreportes.controller;

import com.reservatours.msreportes.dto.ReporteDto;
import com.reservatours.msreportes.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reportes")
public class ReporteController {

    private final ReporteService service;

    @GetMapping
    public ResponseEntity<List<ReporteDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<ReporteDto> findByFecha(@PathVariable String fecha) {
        return ResponseEntity.ok(service.findByFecha(fecha));
    }

    @PostMapping("/generar")
    public ResponseEntity<ReporteDto> generarReporte() {
        return ResponseEntity.ok(service.generarReporteDiario());
    }

    @PostMapping("/enviar-whatsapp")
    public ResponseEntity<String> enviarWhatsApp() {
        service.enviarReporteDiarioWhatsApp();
        return ResponseEntity.ok("Reporte enviado a WhatsApp");
    }
}
