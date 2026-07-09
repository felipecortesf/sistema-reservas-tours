package com.reservatours.mscomunicacionagencia.controller;

import com.reservatours.mscomunicacionagencia.dto.MensajeDto;
import com.reservatours.mscomunicacionagencia.service.MensajeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mensajes")
@Tag(name = "Mensajes Agencia", description = "Comunicacion interna entre clientes y la agencia")
public class MensajeController {

    private final MensajeService service;

    @Operation(summary = "Listar todos los mensajes")
    @GetMapping
    public ResponseEntity<List<MensajeDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar mensaje por ID")
    @GetMapping("/{id}")
    public ResponseEntity<MensajeDto> findById(@PathVariable Long id) {
        MensajeDto dto = service.findById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar mensajes por ID de reserva")
    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<List<MensajeDto>> findByReservaId(@PathVariable Long reservaId) {
        return ResponseEntity.ok(service.findByReservaId(reservaId));
    }

    @Operation(summary = "Registrar una pregunta del cliente hacia Kary")
    @PostMapping("/cliente-pregunta")
    public ResponseEntity<MensajeDto> clientePregunta(
            @RequestParam Long reservaId,
            @RequestParam String telefono,
            @RequestParam String mensaje) {
        return ResponseEntity.ok(service.clientePreguntaKary(reservaId, telefono, mensaje));
    }

    @Operation(summary = "Registrar la respuesta de Kary al cliente")
    @PostMapping("/kary-responde")
    public ResponseEntity<MensajeDto> karyResponde(
            @RequestParam Long reservaId,
            @RequestParam String mensaje) {
        return ResponseEntity.ok(service.karyRespondeCliente(reservaId, mensaje));
    }

    @Operation(summary = "Registrar una alerta de Kary hacia Felipe")
    @PostMapping("/kary-alerta-felipe")
    public ResponseEntity<MensajeDto> karyAlertaFelipe(
            @RequestParam Long reservaId,
            @RequestParam String mensaje) {
        return ResponseEntity.ok(service.karyAlertaFelipe(reservaId, mensaje));
    }

    @Operation(summary = "Registrar una llamada de Felipe al cliente")
    @PostMapping("/felipe-llama-cliente")
    public ResponseEntity<MensajeDto> felipeLlamaCliente(
            @RequestParam Long reservaId,
            @RequestParam String mensaje) {
        return ResponseEntity.ok(service.felipeLlamaCliente(reservaId, mensaje));
    }
}
