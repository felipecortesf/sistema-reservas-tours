package com.reservatours.msnotificaciones.controller;

import com.reservatours.msnotificaciones.dto.NotificacionDto;
import com.reservatours.msnotificaciones.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notificaciones")
public class NotificacionController {

    private final NotificacionService service;

    @GetMapping
    public ResponseEntity<List<NotificacionDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionDto> findById(@PathVariable Long id) {
        NotificacionDto dto = service.findById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<List<NotificacionDto>> findByTelefono(@PathVariable String telefono) {
        return ResponseEntity.ok(service.findByTelefono(telefono));
    }

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<List<NotificacionDto>> findByReservaId(@PathVariable Long reservaId) {
        return ResponseEntity.ok(service.findByReservaId(reservaId));
    }

    @PostMapping("/enviar")
    public ResponseEntity<NotificacionDto> enviar(@Valid @RequestBody NotificacionDto dto) {
        return ResponseEntity.ok(service.enviarNotificacion(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }
}
