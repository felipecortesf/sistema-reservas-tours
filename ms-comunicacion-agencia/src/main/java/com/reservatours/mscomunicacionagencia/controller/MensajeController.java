package com.reservatours.mscomunicacionagencia.controller;

import com.reservatours.mscomunicacionagencia.dto.MensajeDto;
import com.reservatours.mscomunicacionagencia.service.MensajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mensajes")
public class MensajeController {

    private final MensajeService service;

    @GetMapping
    public ResponseEntity<List<MensajeDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MensajeDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<List<MensajeDto>> findByReservaId(@PathVariable Long reservaId) {
        return ResponseEntity.ok(service.findByReservaId(reservaId));
    }

    @PostMapping("/cliente-pregunta")
    public ResponseEntity<MensajeDto> clientePregunta(
            @RequestParam Long reservaId,
            @RequestParam String telefono,
            @RequestParam String mensaje) {
        return ResponseEntity.ok(service.clientePreguntaKary(reservaId, telefono, mensaje));
    }

    @PostMapping("/kary-responde")
    public ResponseEntity<MensajeDto> karyResponde(
            @RequestParam Long reservaId,
            @RequestParam String mensaje) {
        return ResponseEntity.ok(service.karyRespondeCliente(reservaId, mensaje));
    }

    @PostMapping("/kary-alerta-felipe")
    public ResponseEntity<MensajeDto> karyAlertaFelipe(
            @RequestParam Long reservaId,
            @RequestParam String mensaje) {
        return ResponseEntity.ok(service.karyAlertaFelipe(reservaId, mensaje));
    }

    @PostMapping("/felipe-llama-cliente")
    public ResponseEntity<MensajeDto> felipeLlamaCliente(
            @RequestParam Long reservaId,
            @RequestParam String mensaje) {
        return ResponseEntity.ok(service.felipeLlamaCliente(reservaId, mensaje));
    }
}
