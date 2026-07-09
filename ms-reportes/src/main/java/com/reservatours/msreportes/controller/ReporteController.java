package com.reservatours.msreportes.controller;

import com.reservatours.msreportes.dto.ReporteDto;
import com.reservatours.msreportes.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reportes")
@Tag(name = "Reportes", description = "Generacion y envio de reportes diarios de operacion")
public class ReporteController {

    private final ReporteService service;

    @Operation(summary = "Listar todos los reportes")
    @GetMapping
    public ResponseEntity<List<ReporteDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar reporte por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReporteDto> findById(@PathVariable Long id) {
        ReporteDto dto = service.findById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar reporte por fecha")
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<ReporteDto> findByFecha(@PathVariable String fecha) {
        return ResponseEntity.ok(service.findByFecha(fecha));
    }

    @Operation(summary = "Generar el reporte diario")
    @PostMapping("/generar")
    public ResponseEntity<ReporteDto> generarReporte() {
        return ResponseEntity.ok(service.generarReporteDiario());
    }

    @Operation(summary = "Generar y enviar el reporte diario por WhatsApp")
    @PostMapping("/enviar-whatsapp")
    public ResponseEntity<String> enviarWhatsApp() {
        service.enviarReporteDiarioWhatsApp();
        return ResponseEntity.ok("Reporte enviado a WhatsApp");
    }
}
