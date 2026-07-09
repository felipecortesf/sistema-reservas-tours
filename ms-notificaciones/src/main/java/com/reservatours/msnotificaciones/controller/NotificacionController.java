package com.reservatours.msnotificaciones.controller;

import com.reservatours.msnotificaciones.dto.NotificacionDto;
import com.reservatours.msnotificaciones.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notificaciones")
@Tag(name = "Notificaciones", description = "Gestion y envio de notificaciones a clientes")
public class NotificacionController {

    private final NotificacionService service;

    @Operation(summary = "Listar todas las notificaciones")
    @GetMapping
    public ResponseEntity<List<NotificacionDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar notificacion por ID")
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionDto> findById(@PathVariable Long id) {
        NotificacionDto dto = service.findById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar notificaciones por telefono del cliente")
    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<List<NotificacionDto>> findByTelefono(@PathVariable String telefono) {
        return ResponseEntity.ok(service.findByTelefono(telefono));
    }

    @Operation(summary = "Buscar notificaciones por ID de reserva")
    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<List<NotificacionDto>> findByReservaId(@PathVariable Long reservaId) {
        return ResponseEntity.ok(service.findByReservaId(reservaId));
    }

    @Operation(summary = "Enviar una nueva notificacion")
    @PostMapping("/enviar")
    public ResponseEntity<NotificacionDto> enviar(@Valid @RequestBody NotificacionDto dto) {
        return ResponseEntity.ok(service.enviarNotificacion(dto));
    }

    @Operation(summary = "Eliminar una notificacion por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }
}
