package com.reservatours.msembarques.controller;

import com.reservatours.msembarques.dto.EmbarqueDto;
import com.reservatours.msembarques.service.EmbarqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/embarques")
@Tag(name = "Embarques", description = "Gestion de embarques y traslados de los tours")
public class EmbarqueController {

    private final EmbarqueService service;

    @Operation(summary = "Listar todos los embarques")
    @GetMapping
    public ResponseEntity<List<EmbarqueDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar embarque por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EmbarqueDto> findById(@PathVariable Long id) {
        EmbarqueDto dto = service.findById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar embarques por fecha")
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<EmbarqueDto>> findByFecha(@PathVariable String fecha) {
        return ResponseEntity.ok(service.findByFecha(fecha));
    }

    @Operation(summary = "Buscar embarques por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EmbarqueDto>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.findByEstado(estado));
    }

    @Operation(summary = "Crear un nuevo embarque")
    @PostMapping
    public ResponseEntity<EmbarqueDto> save(@Valid @RequestBody EmbarqueDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @Operation(summary = "Actualizar el estado de un embarque")
    @PutMapping("/{id}/estado")
    public ResponseEntity<EmbarqueDto> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado,
            @RequestParam(required = false) String observaciones) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado, observaciones));
    }

    @Operation(summary = "Reportar un retraso en el embarque")
    @PutMapping("/{id}/retraso")
    public ResponseEntity<EmbarqueDto> reportarRetraso(
            @PathVariable Long id,
            @RequestParam String horaReal,
            @RequestParam String observaciones) {
        return ResponseEntity.ok(service.reportarRetraso(id, horaReal, observaciones));
    }

    @Operation(summary = "Eliminar un embarque por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }
}
