package com.reservatours.msreservas.controller;

import com.reservatours.msreservas.dto.ReservaDto;
import com.reservatours.msreservas.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    private final ReservaService service;

    @GetMapping
    public ResponseEntity<List<ReservaDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDto> findById(@PathVariable Long id) {
        ReservaDto dto = service.findById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<ReservaDto>> findByFecha(@PathVariable String fecha) {
        return ResponseEntity.ok(service.findByFecha(fecha));
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<List<ReservaDto>> findByTelefono(@PathVariable String telefono) {
        return ResponseEntity.ok(service.findByTelefono(telefono));
    }

    @PostMapping
    public ResponseEntity<ReservaDto> save(@Valid @RequestBody ReservaDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }

    @PostMapping("/notificar")
    public ResponseEntity<String> notificar() {
        service.enviarNotificacionesDiaSiguiente();
        return ResponseEntity.ok("Notificaciones enviadas");
    }
}
