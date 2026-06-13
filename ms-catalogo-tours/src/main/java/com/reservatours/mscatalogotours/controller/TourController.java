package com.reservatours.mscatalogotours.controller;

import com.reservatours.mscatalogotours.dto.TourDto;
import com.reservatours.mscatalogotours.service.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tours")
@Tag(name = "Tours", description = "Catalogo de tours disponibles en Rio de Janeiro")
public class TourController {

    private final TourService service;

    @Operation(summary = "Listar todos los tours")
    @GetMapping
    public ResponseEntity<List<TourDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Listar tours activos")
    @GetMapping("/activos")
    public ResponseEntity<List<TourDto>> findActivos() {
        return ResponseEntity.ok(service.findActivos());
    }

    @Operation(summary = "Buscar tour por ID")
    @GetMapping("/{id}")
    public ResponseEntity<TourDto> findById(@PathVariable Long id) {
        TourDto tour = service.findById(id);
        if (tour == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(tour);
    }

    @Operation(summary = "Crear un nuevo tour")
    @PostMapping
    public ResponseEntity<TourDto> save(@Valid @RequestBody TourDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @Operation(summary = "Eliminar un tour por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }

    @Operation(summary = "Reducir en 1 los cupos disponibles de un tour")
    @PutMapping("/{id}/reducir-cupo")
    public ResponseEntity<TourDto> reducirCupo(@PathVariable Long id) {
        return ResponseEntity.ok(service.reducirCupo(id));
    }

    @Operation(summary = "Listar tours activos ordenados por precio (Custom Query JPQL)")
    @GetMapping("/ordenados-por-precio")
    public ResponseEntity<List<TourDto>> findToursOrdenadosPorPrecio() {
        return ResponseEntity.ok(service.findToursOrdenadosPorPrecio());
    }

    @Operation(summary = "Precio promedio de tours por destino (Custom Query SQL nativo)")
    @GetMapping("/precio-promedio/{destino}")
    public ResponseEntity<BigDecimal> precioPromedioPorDestino(@PathVariable String destino) {
        return ResponseEntity.ok(service.precioPromedioPorDestino(destino));
    }
}
