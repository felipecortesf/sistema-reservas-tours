package com.reservatours.mspagos.controller;

import com.reservatours.mspagos.dto.PagoDto;
import com.reservatours.mspagos.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pagos")
public class PagoController {

    private final PagoService service;

    @GetMapping
    public ResponseEntity<List<PagoDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDto> findById(@PathVariable Long id) {
        PagoDto pago = service.findById(id);
        if (pago == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pago);
    }

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<List<PagoDto>> findByReservaId(@PathVariable Long reservaId) {
        return ResponseEntity.ok(service.findByReservaId(reservaId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoDto>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.findByEstado(estado));
    }

    @PostMapping
    public ResponseEntity<PagoDto> save(@Valid @RequestBody PagoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}/confirmar")
    public ResponseEntity<PagoDto> confirmarPago(@PathVariable Long id) {
        PagoDto pago = service.confirmarPago(id);
        if (pago == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pago);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }
}
