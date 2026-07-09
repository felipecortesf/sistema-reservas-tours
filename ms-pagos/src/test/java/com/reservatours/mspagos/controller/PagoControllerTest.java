package com.reservatours.mspagos.controller;

import com.reservatours.mspagos.dto.PagoDto;
import com.reservatours.mspagos.service.PagoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoControllerTest {

    @Mock
    private PagoService service;

    @InjectMocks
    private PagoController controller;

    private PagoDto pagoDto;

    @BeforeEach
    void setUp() {
        pagoDto = new PagoDto(1L, 1L, "Juan Perez", "56912345678", "Cristo Redentor",
                new BigDecimal("47827.00"), "TRANSFERENCIA", "PENDIENTE",
                LocalDateTime.now(), null);
    }

    @Test
    void findAll_retorna200() {
        when(service.findAll()).thenReturn(List.of(pagoDto));

        ResponseEntity<List<PagoDto>> response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findById_existente_retorna200() {
        when(service.findById(1L)).thenReturn(pagoDto);

        ResponseEntity<PagoDto> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Juan Perez", response.getBody().getClienteNombre());
    }

    @Test
    void findById_inexistente_retorna404() {
        when(service.findById(99L)).thenReturn(null);

        ResponseEntity<PagoDto> response = controller.findById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findByReservaId_retorna200() {
        when(service.findByReservaId(1L)).thenReturn(List.of(pagoDto));

        ResponseEntity<List<PagoDto>> response = controller.findByReservaId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findByEstado_retorna200() {
        when(service.findByEstado("PENDIENTE")).thenReturn(List.of(pagoDto));

        ResponseEntity<List<PagoDto>> response = controller.findByEstado("PENDIENTE");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void save_retorna201Created() {
        when(service.save(any(PagoDto.class))).thenReturn(pagoDto);

        ResponseEntity<PagoDto> response = controller.save(pagoDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void confirmarPago_existente_retorna200() {
        pagoDto.setEstado("PAGADO");
        when(service.confirmarPago(1L)).thenReturn(pagoDto);

        ResponseEntity<PagoDto> response = controller.confirmarPago(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("PAGADO", response.getBody().getEstado());
    }

    @Test
    void confirmarPago_inexistente_retorna404() {
        when(service.confirmarPago(99L)).thenReturn(null);

        ResponseEntity<PagoDto> response = controller.confirmarPago(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteById_retorna200() {
        when(service.deleteById(1L)).thenReturn(true);

        ResponseEntity<Boolean> response = controller.deleteById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void sumarMontoPorEstado_retorna200() {
        when(service.sumarMontoPorEstado(anyString())).thenReturn(new BigDecimal("100000.00"));

        ResponseEntity<BigDecimal> response = controller.sumarMontoPorEstado("PAGADO");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void topClientesPorMontoPagado_retorna200() {
        when(service.topClientesPorMontoPagado(5)).thenReturn(List.of(Map.of("cliente", "Juan Perez")));

        ResponseEntity<List<Map<String, Object>>> response = controller.topClientesPorMontoPagado(5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
