package com.reservatours.mspagos.controller;

import com.reservatours.mspagos.dto.PagoDto;
import com.reservatours.mspagos.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pagos", description = "Gestion de pagos de reservas")
public class PagoController {

    private final PagoService service;

    @Operation(summary = "Listar todos los pagos")
    @GetMapping
    public ResponseEntity<List<PagoDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar pago por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PagoDto> findById(@PathVariable Long id) {
        PagoDto pago = service.findById(id);
        if (pago == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pago);
    }

    @Operation(summary = "Buscar pagos por ID de reserva")
    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<List<PagoDto>> findByReservaId(@PathVariable Long reservaId) {
        return ResponseEntity.ok(service.findByReservaId(reservaId));
    }

    @Operation(summary = "Buscar pagos por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PagoDto>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.findByEstado(estado));
    }

    @Operation(summary = "Crear un nuevo pago")
    @PostMapping
    public ResponseEntity<PagoDto> save(@Valid @RequestBody PagoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @Operation(summary = "Confirmar un pago (PENDIENTE -> PAGADO)")
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<PagoDto> confirmarPago(@PathVariable Long id) {
        PagoDto pago = service.confirmarPago(id);
        if (pago == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pago);
    }

    @Operation(summary = "Eliminar un pago por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }

    @Operation(summary = "Sumar el monto total de pagos por estado (Custom Query JPQL)")
    @GetMapping("/total/{estado}")
    public ResponseEntity<BigDecimal> sumarMontoPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.sumarMontoPorEstado(estado));
    }

    @Operation(summary = "Top clientes por monto pagado (Custom Query SQL nativo)")
    @GetMapping("/top-clientes/{limite}")
    public ResponseEntity<List<Map<String, Object>>> topClientesPorMontoPagado(@PathVariable int limite) {
        return ResponseEntity.ok(service.topClientesPorMontoPagado(limite));
    }
}
