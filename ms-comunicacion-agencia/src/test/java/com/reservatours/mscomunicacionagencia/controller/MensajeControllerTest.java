package com.reservatours.mscomunicacionagencia.controller;

import com.reservatours.mscomunicacionagencia.dto.MensajeDto;
import com.reservatours.mscomunicacionagencia.service.MensajeService;
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
class MensajeControllerTest {

    @Mock
    private MensajeService service;

    @InjectMocks
    private MensajeController controller;

    private MensajeDto mensajeDto;

    @BeforeEach
    void setUp() {
        mensajeDto = new MensajeDto(1L, "cliente", "kary", "999111222", "999333444",
                "Hola, tengo una consulta", "pregunta", "enviado", 10L,
                LocalDateTime.of(2026, 7, 9, 10, 0));
    }

    @Test
    void findAll_retornaListaConStatus200() {
        when(service.findAll()).thenReturn(List.of(mensajeDto));

        ResponseEntity<List<MensajeDto>> response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findById_existente_retorna200() {
        when(service.findById(1L)).thenReturn(mensajeDto);

        ResponseEntity<MensajeDto> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Hola, tengo una consulta", response.getBody().getContenido());
    }

    @Test
    void findById_inexistente_retorna404() {
        when(service.findById(99L)).thenReturn(null);

        ResponseEntity<MensajeDto> response = controller.findById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findByReservaId_retornaListaConStatus200() {
        when(service.findByReservaId(10L)).thenReturn(List.of(mensajeDto));

        ResponseEntity<List<MensajeDto>> response = controller.findByReservaId(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(service, times(1)).findByReservaId(10L);
    }

    @Test
    void clientePregunta_retorna200ConMensajeCreado() {
        when(service.clientePreguntaKary(10L, "999111222", "Hola, tengo una consulta"))
                .thenReturn(mensajeDto);

        ResponseEntity<MensajeDto> response = controller.clientePregunta(10L, "999111222", "Hola, tengo una consulta");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(service, times(1)).clientePreguntaKary(10L, "999111222", "Hola, tengo una consulta");
    }

    @Test
    void karyResponde_retorna200ConMensajeCreado() {
        when(service.karyRespondeCliente(10L, "Claro, te ayudo con eso")).thenReturn(mensajeDto);

        ResponseEntity<MensajeDto> response = controller.karyResponde(10L, "Claro, te ayudo con eso");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(service, times(1)).karyRespondeCliente(10L, "Claro, te ayudo con eso");
    }

    @Test
    void karyAlertaFelipe_retorna200ConMensajeCreado() {
        when(service.karyAlertaFelipe(10L, "Cliente necesita atencion urgente")).thenReturn(mensajeDto);

        ResponseEntity<MensajeDto> response = controller.karyAlertaFelipe(10L, "Cliente necesita atencion urgente");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(service, times(1)).karyAlertaFelipe(10L, "Cliente necesita atencion urgente");
    }

    @Test
    void felipeLlamaCliente_retorna200ConMensajeCreado() {
        when(service.felipeLlamaCliente(10L, "Llamada realizada exitosamente")).thenReturn(mensajeDto);

        ResponseEntity<MensajeDto> response = controller.felipeLlamaCliente(10L, "Llamada realizada exitosamente");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(service, times(1)).felipeLlamaCliente(10L, "Llamada realizada exitosamente");
    }
}
