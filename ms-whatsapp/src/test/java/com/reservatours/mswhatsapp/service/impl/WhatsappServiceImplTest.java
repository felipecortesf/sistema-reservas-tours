package com.reservatours.mswhatsapp.service.impl;

import com.reservatours.mswhatsapp.dto.MensajeWhatsappDto;
import com.reservatours.mswhatsapp.model.MensajeWhatsapp;
import com.reservatours.mswhatsapp.repository.MensajeWhatsappRepository;
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
class WhatsappServiceImplTest {

    @Mock
    private MensajeWhatsappRepository repository;

    @InjectMocks
    private WhatsappServiceImpl service;

    private MensajeWhatsapp mensaje;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "accountSid", "fake-sid");
        ReflectionTestUtils.setField(service, "authToken", "fake-token");
        ReflectionTestUtils.setField(service, "fromNumber", "whatsapp:+14155238886");
        ReflectionTestUtils.setField(service, "telefonoKary", "56922222222");
        ReflectionTestUtils.setField(service, "verifyToken", "mi-token-secreto");

        mensaje = new MensajeWhatsapp(1L, "56933333333", "whatsapp:+14155238886",
                "Juan Perez", "Hola, tengo una duda", "TEXTO", "ENTRANTE",
                "RECIBIDO", false, LocalDateTime.now());
    }

    @Test
    void findById_existente_retornaDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(mensaje));

        MensajeWhatsappDto resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombreRemitente());
    }

    @Test
    void findById_inexistente_retornaNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertNull(service.findById(99L));
    }

    @Test
    void findNoProcessados_retornaSoloNoProcesados() {
        when(repository.findByProcesado(false)).thenReturn(List.of(mensaje));

        List<MensajeWhatsappDto> resultado = service.findNoProcessados();

        assertEquals(1, resultado.size());
        assertFalse(resultado.get(0).getProcesado());
    }

    @Test
    void enviarMensaje_twilioFalla_marcaEstadoErrorYGuarda() {
        when(repository.save(any(MensajeWhatsapp.class))).thenAnswer(i -> {
            MensajeWhatsapp m = (MensajeWhatsapp) i.getArgument(0);
            m.setId(1L);
            return m;
        });

        MensajeWhatsappDto resultado = service.enviarMensaje("56933333333", "Tu reserva esta confirmada");

        assertEquals("ERROR", resultado.getEstado());
        assertEquals("SALIENTE", resultado.getDireccion());
        assertTrue(resultado.getProcesado());
        verify(repository, times(1)).save(any(MensajeWhatsapp.class));
    }

    @Test
    void recibirMensaje_guardaMensajeEntranteYReenviaAKary() {
        when(repository.save(any(MensajeWhatsapp.class))).thenAnswer(i -> {
            MensajeWhatsapp m = (MensajeWhatsapp) i.getArgument(0);
            m.setId(1L);
            return m;
        });

        MensajeWhatsappDto resultado = service.recibirMensaje("56933333333", "Juan Perez", "Hola, tengo una duda");

        assertEquals("ENTRANTE", resultado.getDireccion());
        assertEquals("RECIBIDO", resultado.getEstado());
        assertFalse(resultado.getProcesado());
        // se guarda el mensaje recibido + el reenvio a Kary dentro de enviarMensaje
        verify(repository, times(2)).save(any(MensajeWhatsapp.class));
    }

    @Test
    void verificarWebhook_tokenCorrectoYModeSubscribe_retornaChallenge() {
        String resultado = service.verificarWebhook("subscribe", "mi-token-secreto", "challenge123");

        assertEquals("challenge123", resultado);
    }

    @Test
    void verificarWebhook_tokenIncorrecto_retornaError() {
        String resultado = service.verificarWebhook("subscribe", "token-erroneo", "challenge123");

        assertEquals("Error de verificacion", resultado);
    }

    @Test
    void procesarWebhook_retornaOk() {
        assertEquals("OK", service.procesarWebhook("{}"));
    }
}
