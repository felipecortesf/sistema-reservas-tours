package com.reservatours.msnotificacionespush.controller;

import com.reservatours.msnotificacionespush.dto.NotificacionPushDto;
import com.reservatours.msnotificacionespush.service.NotificacionPushService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notificaciones-push")
@Tag(name = "Notificaciones Push", description = "Gestion y envio de notificaciones push a clientes")
public class NotificacionPushController {

    private final NotificacionPushService service;

    @Operation(summary = "Listar todas las notificaciones push")
    @GetMapping
    public ResponseEntity<List<NotificacionPushDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar notificacion push por ID")
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionPushDto> findById(@PathVariable Long id) {
        NotificacionPushDto dto = service.findById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar notificaciones push por telefono del cliente")
    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<List<NotificacionPushDto>> findByTelefono(@PathVariable String telefono) {
        return ResponseEntity.ok(service.findByTelefono(telefono));
    }

    @Operation(summary = "Listar notificaciones push no leidas por telefono")
    @GetMapping("/no-leidas/{telefono}")
    public ResponseEntity<List<NotificacionPushDto>> findNoLeidas(@PathVariable String telefono) {
        return ResponseEntity.ok(service.findNoLeidas(telefono));
    }

    @Operation(summary = "Enviar una nueva notificacion push")
    @PostMapping("/enviar")
    public ResponseEntity<NotificacionPushDto> enviar(@Valid @RequestBody NotificacionPushDto dto) {
        return ResponseEntity.ok(service.enviar(dto));
    }

    @Operation(summary = "Marcar una notificacion push como leida")
    @PutMapping("/{id}/leida")
    public ResponseEntity<NotificacionPushDto> marcarLeida(@PathVariable Long id) {
        return ResponseEntity.ok(service.marcarLeida(id));
    }

    @Operation(summary = "Eliminar una notificacion push por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }
}
