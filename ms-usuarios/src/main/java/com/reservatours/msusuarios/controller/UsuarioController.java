package com.reservatours.msusuarios.controller;

import com.reservatours.msusuarios.dto.UsuarioDto;
import com.reservatours.msusuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Gestion de usuarios del sistema")
public class UsuarioController {

    private final UsuarioService service;

    @Operation(summary = "Listar todos los usuarios")
    @GetMapping
    public ResponseEntity<List<UsuarioDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> findById(@PathVariable Long id) {
        UsuarioDto dto = service.findById(id);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Buscar usuario por email")
    @GetMapping("/email/{email}")
    public ResponseEntity<UsuarioDto> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }

    @Operation(summary = "Crear un nuevo usuario")
    @PostMapping
    public ResponseEntity<UsuarioDto> save(@Valid @RequestBody UsuarioDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @Operation(summary = "Eliminar un usuario por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteById(id));
    }
}
