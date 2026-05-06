package com.reservatours.mscatalogotours.controller;

import com.reservatours.mscatalogotours.dto.TourDto;
import com.reservatours.mscatalogotours.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tours")
public class TourController {

    private final TourService service;

    @GetMapping
    public ResponseEntity<List<TourDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<TourDto>> findActivos() {
        return ResponseEntity.ok(service.findActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<TourDto> save(@RequestBody TourDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }

    @PutMapping("/{id}/reducir-cupo")
    public ResponseEntity<TourDto> reducirCupo(@PathVariable Long id) {
        return ResponseEntity.ok(service.reducirCupo(id));
    }
}
