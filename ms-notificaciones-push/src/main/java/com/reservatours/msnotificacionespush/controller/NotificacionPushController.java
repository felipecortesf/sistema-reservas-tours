package com.reservatours.msnotificacionespush.controller;

import com.reservatours.msnotificacionespush.dto.NotificacionPushDto;
import com.reservatours.msnotificacionespush.service.NotificacionPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notificaciones-push")
public class NotificacionPushController {

    private final NotificacionPushService service;

    @GetMapping
    public ResponseEntity<List<NotificacionPushDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionPushDto> findById(@PathVariable Long id) {
        NotificacionPushDto dto = service.findById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<List<NotificacionPushDto>> findByTelefono(@PathVariable String telefono) {
        return ResponseEntity.ok(service.findByTelefono(telefono));
    }

    @GetMapping("/no-leidas/{telefono}")
    public ResponseEntity<List<NotificacionPushDto>> findNoLeidas(@PathVariable String telefono) {
        return ResponseEntity.ok(service.findNoLeidas(telefono));
    }

    @PostMapping("/enviar")
    public ResponseEntity<NotificacionPushDto> enviar(@Valid @RequestBody NotificacionPushDto dto) {
        return ResponseEntity.ok(service.enviar(dto));
    }

    @PutMapping("/{id}/leida")
    public ResponseEntity<NotificacionPushDto> marcarLeida(@PathVariable Long id) {
        return ResponseEntity.ok(service.marcarLeida(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }
}
