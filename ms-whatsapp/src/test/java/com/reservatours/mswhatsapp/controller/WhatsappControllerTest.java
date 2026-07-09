package com.reservatours.mswhatsapp.controller;

import com.reservatours.mswhatsapp.dto.MensajeWhatsappDto;
import com.reservatours.mswhatsapp.service.WhatsappService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WhatsappControllerTest {

    @Mock
    private WhatsappService service;

    @InjectMocks
    private WhatsappController controller;

    private MensajeWhatsappDto mensajeDto;

    @BeforeEach
    void setUp() {
        mensajeDto = new MensajeWhatsappDto(1L, "999111222", "999333444", "Felipe",
                "Hola, este es un mensaje de prueba", "texto", "saliente", "enviado", true,
                LocalDateTime.of(2026, 7, 9, 10, 0));
    }

    @Test
    void findAll_retornaListaConStatus200() {
        when(service.findAll()).thenReturn(List.of(mensajeDto));

        ResponseEntity<List<MensajeWhatsappDto>> response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findNoProcessados_retornaListaConStatus200() {
        when(service.findNoProcessados()).thenReturn(List.of(mensajeDto));

        ResponseEntity<List<MensajeWhatsappDto>> response = controller.findNoProcessados();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).findNoProcessados();
    }

    @Test
    void enviar_retorna200ConMensajeEnviado() {
        when(service.enviarMensaje("999111222", "Hola, este es un mensaje de prueba"))
                .thenReturn(mensajeDto);

        ResponseEntity<MensajeWhatsappDto> response = controller.enviar("999111222", "Hola, este es un mensaje de prueba");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(service, times(1)).enviarMensaje("999111222", "Hola, este es un mensaje de prueba");
    }

    @Test
    void recibir_retorna200ConMensajeRegistrado() {
        when(service.recibirMensaje("999111222", "Felipe", "Hola, este es un mensaje de prueba"))
                .thenReturn(mensajeDto);

        ResponseEntity<MensajeWhatsappDto> response = controller.recibir("999111222", "Felipe", "Hola, este es un mensaje de prueba");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(service, times(1)).recibirMensaje("999111222", "Felipe", "Hola, este es un mensaje de prueba");
    }

    @Test
    void verificarWebhook_retorna200ConChallenge() {
        when(service.verificarWebhook("subscribe", "token123", "challenge456"))
                .thenReturn("challenge456");

        ResponseEntity<String> response = controller.verificarWebhook("subscribe", "token123", "challenge456");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("challenge456", response.getBody());
    }

    @Test
    void recibirWebhook_retorna200ConRespuestaProcesada() {
        when(service.procesarWebhook("body-webhook-twilio")).thenReturn("OK");

        ResponseEntity<String> response = controller.recibirWebhook("body-webhook-twilio");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OK", response.getBody());
        verify(service, times(1)).procesarWebhook("body-webhook-twilio");
    }
}
