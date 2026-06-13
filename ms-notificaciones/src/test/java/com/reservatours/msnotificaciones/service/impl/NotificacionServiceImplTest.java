package com.reservatours.msnotificaciones.service.impl;

import com.reservatours.msnotificaciones.dto.NotificacionDto;
import com.reservatours.msnotificaciones.model.Notificacion;
import com.reservatours.msnotificaciones.repository.NotificacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacionServiceImplTest {

    @Mock
    private NotificacionRepository repository;

    @InjectMocks
    private NotificacionServiceImpl service;

    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "whatsappToken", "fake-token");
        ReflectionTestUtils.setField(service, "phoneNumberId", "123456");

        notificacion = new Notificacion(1L, "56933333333", "Juan Perez",
                "Su tour es mañana", "WHATSAPP", "PENDIENTE", LocalDateTime.now(), 1L);
    }

    @Test
    void findById_existente_retornaNotificacionDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(notificacion));

        NotificacionDto resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getDestinatarioNombre());
    }

    @Test
    void findById_inexistente_retornaNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertNull(service.findById(99L));
    }

    @Test
    void findByTelefono_retornaNotificacionesDeEseTelefono() {
        when(repository.findByDestinatarioTelefono("56933333333")).thenReturn(List.of(notificacion));

        List<NotificacionDto> resultado = service.findByTelefono("56933333333");

        assertEquals(1, resultado.size());
    }

    @Test
    void findByReservaId_retornaNotificacionesDeEsaReserva() {
        when(repository.findByReservaId(1L)).thenReturn(List.of(notificacion));

        List<NotificacionDto> resultado = service.findByReservaId(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getReservaId());
    }

    @Test
    void enviarNotificacion_fallaEnvioReal_marcaEstadoErrorYGuarda() {
        NotificacionDto dto = new NotificacionDto(null, "56933333333", "Juan Perez",
                "Su tour es mañana", "WHATSAPP", "PENDIENTE", null, 1L);

        when(repository.save(any(Notificacion.class))).thenAnswer(i -> {
            Notificacion n = (Notificacion) i.getArgument(0);
            n.setId(1L);
            return n;
        });

        NotificacionDto resultado = service.enviarNotificacion(dto);

        assertEquals("ERROR", resultado.getEstado());
        assertNotNull(resultado.getFechaEnvio());
        verify(repository, times(1)).save(any(Notificacion.class));
    }

    @Test
    void deleteById_existente_retornaTrue() {
        when(repository.existsById(1L)).thenReturn(true);

        Boolean resultado = service.deleteById(1L);

        assertTrue(resultado);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_inexistente_retornaFalse() {
        when(repository.existsById(99L)).thenReturn(false);

        Boolean resultado = service.deleteById(99L);

        assertFalse(resultado);
        verify(repository, never()).deleteById(any());
    }
}
