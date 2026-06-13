package com.reservatours.msreservas.controller;

import com.reservatours.msreservas.dto.ReservaDto;
import com.reservatours.msreservas.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservas")
@Tag(name = "Reservas", description = "Gestion de reservas de tours")
public class ReservaController {

    private final ReservaService service;

    @Operation(summary = "Listar todas las reservas")
    @GetMapping
    public ResponseEntity<List<ReservaDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar reserva por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDto> findById(@PathVariable Long id) {
        ReservaDto dto = service.findById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar reservas por fecha de tour")
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<ReservaDto>> findByFecha(@PathVariable String fecha) {
        return ResponseEntity.ok(service.findByFecha(fecha));
    }

    @Operation(summary = "Buscar reservas por telefono del cliente")
    @GetMapping("/telefono/{telefono}")
    public ResponseEntity<List<ReservaDto>> findByTelefono(@PathVariable String telefono) {
        return ResponseEntity.ok(service.findByTelefono(telefono));
    }

    @Operation(summary = "Crear una nueva reserva")
    @PostMapping
    public ResponseEntity<ReservaDto> save(@Valid @RequestBody ReservaDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @Operation(summary = "Eliminar una reserva por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }

    @Operation(summary = "Disparar manualmente el envio de notificaciones del dia siguiente")
    @PostMapping("/notificar")
    public ResponseEntity<String> notificar() {
        service.enviarNotificacionesDiaSiguiente();
        return ResponseEntity.ok("Notificaciones enviadas");
    }

    @Operation(summary = "Contar reservas confirmadas para un tour (Custom Query JPQL)")
    @GetMapping("/tour/{tourId}/confirmadas")
    public ResponseEntity<Long> contarConfirmadasPorTour(@PathVariable Long tourId) {
        return ResponseEntity.ok(service.contarConfirmadasPorTour(tourId));
    }

    @Operation(summary = "Listar reservas proximas en N dias (Custom Query SQL nativo)")
    @GetMapping("/proximas/{dias}")
    public ResponseEntity<List<ReservaDto>> findReservasProximas(@PathVariable int dias) {
        return ResponseEntity.ok(service.findReservasProximas(dias));
    }
}
