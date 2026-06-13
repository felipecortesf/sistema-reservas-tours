package com.reservatours.mscomunicacionagencia.service.impl;

import com.reservatours.mscomunicacionagencia.dto.MensajeDto;
import com.reservatours.mscomunicacionagencia.model.Mensaje;
import com.reservatours.mscomunicacionagencia.repository.MensajeRepository;
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
class MensajeServiceImplTest {

    @Mock
    private MensajeRepository repository;

    @InjectMocks
    private MensajeServiceImpl service;

    private Mensaje mensaje;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "whatsappToken", "fake-token");
        ReflectionTestUtils.setField(service, "phoneNumberId", "123456");
        ReflectionTestUtils.setField(service, "telefonoFelipe", "56911111111");
        ReflectionTestUtils.setField(service, "telefonoKary", "56922222222");

        mensaje = new Mensaje(1L, "CLIENTE", "KARY", "56933333333", "56922222222",
                "Hola, tengo una duda", "CONSULTA", "ENVIADO", 1L, LocalDateTime.now());
    }

    @Test
    void findById_existente_retornaMensajeDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(mensaje));

        MensajeDto resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals("CONSULTA", resultado.getTipo());
    }

    @Test
    void findById_inexistente_retornaNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertNull(service.findById(99L));
    }

    @Test
    void findByReservaId_retornaMensajesDeEsaReserva() {
        when(repository.findByReservaId(1L)).thenReturn(List.of(mensaje));

        List<MensajeDto> resultado = service.findByReservaId(1L);

        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getReservaId());
    }

    @Test
    void clientePreguntaKary_guardaMensajeConOrigenClienteYDestinoKary() {
        when(repository.save(any(Mensaje.class))).thenAnswer(i -> i.getArgument(0));

        MensajeDto resultado = service.clientePreguntaKary(1L, "56933333333", "Tengo una duda");

        assertEquals("CLIENTE", resultado.getOrigen());
        assertEquals("KARY", resultado.getDestino());
        assertEquals("CONSULTA", resultado.getTipo());
        assertEquals("ENVIADO", resultado.getEstado());
        verify(repository, times(1)).save(any(Mensaje.class));
    }

    @Test
    void karyRespondeCliente_guardaMensajeConOrigenKaryYDestinoCliente() {
        when(repository.save(any(Mensaje.class))).thenAnswer(i -> i.getArgument(0));

        MensajeDto resultado = service.karyRespondeCliente(1L, "Su reserva esta confirmada");

        assertEquals("KARY", resultado.getOrigen());
        assertEquals("CLIENTE", resultado.getDestino());
        assertEquals("RESPUESTA", resultado.getTipo());
    }

    @Test
    void karyAlertaFelipe_guardaMensajeConTipoAlerta() {
        when(repository.save(any(Mensaje.class))).thenAnswer(i -> i.getArgument(0));

        MensajeDto resultado = service.karyAlertaFelipe(1L, "Cliente no llega al punto de encuentro");

        assertEquals("KARY", resultado.getOrigen());
        assertEquals("FELIPE", resultado.getDestino());
        assertEquals("ALERTA", resultado.getTipo());
    }

    @Test
    void felipeLlamaCliente_guardaMensajeConTipoLlamada() {
        when(repository.save(any(Mensaje.class))).thenAnswer(i -> i.getArgument(0));

        MensajeDto resultado = service.felipeLlamaCliente(1L, "Llamada realizada al cliente");

        assertEquals("FELIPE", resultado.getOrigen());
        assertEquals("CLIENTE", resultado.getDestino());
        assertEquals("LLAMADA", resultado.getTipo());
    }
}
