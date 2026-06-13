package com.reservatours.msnotificacionespush.service.impl;

import com.reservatours.msnotificacionespush.dto.NotificacionPushDto;
import com.reservatours.msnotificacionespush.model.NotificacionPush;
import com.reservatours.msnotificacionespush.repository.NotificacionPushRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionPushServiceImplTest {

    @Mock
    private NotificacionPushRepository repository;

    @InjectMocks
    private NotificacionPushServiceImpl service;

    private NotificacionPush notificacion;

    @BeforeEach
    void setUp() {
        notificacion = new NotificacionPush(1L, "Reserva confirmada", "Tu reserva fue confirmada",
                1L, "56933333333", "INFO", "ENVIADO", LocalDateTime.now(), false);
    }

    @Test
    void findById_existente_retornaDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));

        NotificacionPushDto resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals("Reserva confirmada", resultado.getTitulo());
    }

    @Test
    void findById_inexistente_retornaNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertNull(service.findById(99L));
    }

    @Test
    void enviar_asignaEstadoEnviadoYLeidaFalse() {
        NotificacionPushDto dto = new NotificacionPushDto(null, "Nuevo mensaje", "Tienes un mensaje nuevo",
                1L, "56933333333", "INFO", null, null, null);

        when(repository.save(any(NotificacionPush.class))).thenAnswer(i -> i.getArgument(0));

        NotificacionPushDto resultado = service.enviar(dto);

        assertEquals("ENVIADO", resultado.getEstado());
        assertFalse(resultado.getLeida());
        assertNotNull(resultado.getFechaEnvio());
    }

    @Test
    void marcarLeida_existente_actualizaLeidaATrue() {
        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));
        when(repository.save(any(NotificacionPush.class))).thenAnswer(i -> i.getArgument(0));

        NotificacionPushDto resultado = service.marcarLeida(1L);

        assertTrue(resultado.getLeida());
    }

    @Test
    void marcarLeida_inexistente_retornaNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertNull(service.marcarLeida(99L));
    }

    @Test
    void findNoLeidas_filtraPorTelefonoYNoLeidas() {
        when(repository.findByLeida(false)).thenReturn(List.of(notificacion));

        List<NotificacionPushDto> resultado = service.findNoLeidas("56933333333");

        assertEquals(1, resultado.size());
    }

    @Test
    void deleteById_inexistente_retornaFalse() {
        when(repository.existsById(99L)).thenReturn(false);

        assertFalse(service.deleteById(99L));
    }
}
