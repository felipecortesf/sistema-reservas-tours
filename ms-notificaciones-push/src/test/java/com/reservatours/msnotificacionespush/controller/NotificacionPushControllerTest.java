package com.reservatours.msnotificacionespush.controller;

import com.reservatours.msnotificacionespush.dto.NotificacionPushDto;
import com.reservatours.msnotificacionespush.service.NotificacionPushService;
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
class NotificacionPushControllerTest {

    @Mock
    private NotificacionPushService service;

    @InjectMocks
    private NotificacionPushController controller;

    private NotificacionPushDto notificacionPushDto;

    @BeforeEach
    void setUp() {
        notificacionPushDto = new NotificacionPushDto(1L, "Reserva confirmada",
                "Su reserva ha sido confirmada exitosamente", 5L, "999888777",
                "PUSH", "ENVIADA", LocalDateTime.of(2026, 7, 9, 10, 0), false);
    }

    @Test
    void findAll_retornaListaConStatus200() {
        when(service.findAll()).thenReturn(List.of(notificacionPushDto));

        ResponseEntity<List<NotificacionPushDto>> response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findById_existente_retorna200() {
        when(service.findById(1L)).thenReturn(notificacionPushDto);

        ResponseEntity<NotificacionPushDto> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Reserva confirmada", response.getBody().getTitulo());
    }

    @Test
    void findById_inexistente_retorna404() {
        when(service.findById(99L)).thenReturn(null);

        ResponseEntity<NotificacionPushDto> response = controller.findById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findByTelefono_retorna200() {
        when(service.findByTelefono(anyString())).thenReturn(List.of(notificacionPushDto));

        ResponseEntity<List<NotificacionPushDto>> response = controller.findByTelefono("999888777");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findNoLeidas_retorna200() {
        when(service.findNoLeidas(anyString())).thenReturn(List.of(notificacionPushDto));

        ResponseEntity<List<NotificacionPushDto>> response = controller.findNoLeidas("999888777");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).findNoLeidas("999888777");
    }

    @Test
    void enviar_retorna200ConNotificacionCreada() {
        when(service.enviar(any(NotificacionPushDto.class))).thenReturn(notificacionPushDto);

        ResponseEntity<NotificacionPushDto> response = controller.enviar(notificacionPushDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void marcarLeida_retorna200() {
        when(service.marcarLeida(1L)).thenReturn(notificacionPushDto);

        ResponseEntity<NotificacionPushDto> response = controller.marcarLeida(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).marcarLeida(1L);
    }

    @Test
    void deleteById_retorna200() {
        when(service.deleteById(1L)).thenReturn(true);

        ResponseEntity<Boolean> response = controller.deleteById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }
}
