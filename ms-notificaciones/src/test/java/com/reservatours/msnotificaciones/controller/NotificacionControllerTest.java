package com.reservatours.msnotificaciones.controller;

import com.reservatours.msnotificaciones.dto.NotificacionDto;
import com.reservatours.msnotificaciones.service.NotificacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionControllerTest {

    @Mock
    private NotificacionService service;

    @InjectMocks
    private NotificacionController controller;

    private NotificacionDto notificacionDto;

    @BeforeEach
    void setUp() {
        notificacionDto = new NotificacionDto(1L, "999888777", "Juan Perez",
                "Su reserva ha sido confirmada", "SMS", "ENVIADA",
                LocalDateTime.of(2026, 7, 9, 10, 0), 10L);
    }

    @Test
    void findAll_retornaListaConStatus200() {
        when(service.findAll()).thenReturn(List.of(notificacionDto));

        ResponseEntity<List<NotificacionDto>> response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findById_existente_retorna200() {
        when(service.findById(1L)).thenReturn(notificacionDto);

        ResponseEntity<NotificacionDto> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Juan Perez", response.getBody().getDestinatarioNombre());
    }

    @Test
    void findById_inexistente_retorna404() {
        when(service.findById(99L)).thenReturn(null);

        ResponseEntity<NotificacionDto> response = controller.findById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findByTelefono_retorna200() {
        when(service.findByTelefono(anyString())).thenReturn(List.of(notificacionDto));

        ResponseEntity<List<NotificacionDto>> response = controller.findByTelefono("999888777");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findByReservaId_retorna200() {
        when(service.findByReservaId(10L)).thenReturn(List.of(notificacionDto));

        ResponseEntity<List<NotificacionDto>> response = controller.findByReservaId(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).findByReservaId(10L);
    }

    @Test
    void enviar_retorna200ConNotificacionCreada() {
        when(service.enviarNotificacion(any(NotificacionDto.class))).thenReturn(notificacionDto);

        ResponseEntity<NotificacionDto> response = controller.enviar(notificacionDto);

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
