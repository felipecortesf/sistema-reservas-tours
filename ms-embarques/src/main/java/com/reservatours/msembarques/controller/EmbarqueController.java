package com.reservatours.msembarques.controller;

import com.reservatours.msembarques.dto.EmbarqueDto;
import com.reservatours.msembarques.service.EmbarqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/embarques")
public class EmbarqueController {

    private final EmbarqueService service;

    @GetMapping
    public ResponseEntity<List<EmbarqueDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmbarqueDto> findById(@PathVariable Long id) {
        EmbarqueDto dto = service.findById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<EmbarqueDto>> findByFecha(@PathVariable String fecha) {
        return ResponseEntity.ok(service.findByFecha(fecha));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EmbarqueDto>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.findByEstado(estado));
    }

    @PostMapping
    public ResponseEntity<EmbarqueDto> save(@Valid @RequestBody EmbarqueDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<EmbarqueDto> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado,
            @RequestParam(required = false) String observaciones) {
        return ResponseEntity.ok(service.actualizarEstado(id, estado, observaciones));
    }

    @PutMapping("/{id}/retraso")
    public ResponseEntity<EmbarqueDto> reportarRetraso(
            @PathVariable Long id,
            @RequestParam String horaReal,
            @RequestParam String observaciones) {
        return ResponseEntity.ok(service.reportarRetraso(id, horaReal, observaciones));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }
}
