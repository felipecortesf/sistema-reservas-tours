package com.reservatours.msusuarios.controller;

import com.reservatours.msusuarios.dto.UsuarioDto;
import com.reservatours.msusuarios.model.Rol;
import com.reservatours.msusuarios.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService service;

    @InjectMocks
    private UsuarioController controller;

    private UsuarioDto usuarioDto;
    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol(1L, "ADMIN");
        usuarioDto = new UsuarioDto(1L, "Felipe", "Cortes", "felipe@email.com",
                "hashedPass", "56912345678", rol);
    }

    @Test
    void findAll_retornaListaConStatus200() {
        when(service.findAll()).thenReturn(List.of(usuarioDto));

        ResponseEntity<List<UsuarioDto>> response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findById_existente_retorna200() {
        when(service.findById(1L)).thenReturn(usuarioDto);

        ResponseEntity<UsuarioDto> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Felipe", response.getBody().getNombre());
    }

    @Test
    void findById_inexistente_retorna404() {
        when(service.findById(99L)).thenReturn(null);

        ResponseEntity<UsuarioDto> response = controller.findById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findByEmail_existente_retorna200() {
        when(service.findByEmail(anyString())).thenReturn(usuarioDto);

        ResponseEntity<UsuarioDto> response = controller.findByEmail("felipe@email.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("felipe@email.com", response.getBody().getEmail());
    }

    @Test
    void save_retorna200ConUsuarioCreado() {
        when(service.save(any(UsuarioDto.class))).thenReturn(usuarioDto);

        ResponseEntity<UsuarioDto> response = controller.save(usuarioDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteById_retorna200() {
        when(service.deleteById(1L)).thenReturn(true);

        ResponseEntity<Boolean> response = controller.deleteById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }
}
