package com.reservatours.mspagos.service.impl;

import com.reservatours.mspagos.dto.PagoDto;
import com.reservatours.mspagos.exception.ResourceNotFoundException;
import com.reservatours.mspagos.model.Pago;
import com.reservatours.mspagos.repository.PagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceImplTest {

    @Mock
    private PagoRepository repository;

    @InjectMocks
    private PagoServiceImpl service;

    private Pago pago;

    @BeforeEach
    void setUp() {
        pago = new Pago(1L, 1L, "Juan Perez", "56912345678", "Cristo Redentor",
                new BigDecimal("47827.00"), "TRANSFERENCIA", "PENDIENTE",
                LocalDateTime.now(), null);
    }

    @Test
    void findById_existente_retornaPagoDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(pago));

        PagoDto resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado());
    }

    @Test
    void findById_inexistente_lanzaResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void confirmarPago_existente_cambiaEstadoAPagado() {
        when(repository.findById(1L)).thenReturn(Optional.of(pago));
        when(repository.save(any(Pago.class))).thenAnswer(i -> i.getArgument(0));

        PagoDto resultado = service.confirmarPago(1L);

        assertEquals("PAGADO", resultado.getEstado());
        verify(repository, times(1)).save(any(Pago.class));
    }

    @Test
    void confirmarPago_inexistente_lanzaResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.confirmarPago(99L));
        verify(repository, never()).save(any(Pago.class));
    }

    @Test
    void save_pagoNuevo_asignaEstadoPendientePorDefecto() {
        PagoDto nuevoPago = new PagoDto(null, 2L, "Maria Garcia", "56987654321",
                "Pao de Acucar", new BigDecimal("44148.00"), "EFECTIVO", null, null, null);

        when(repository.save(any(Pago.class))).thenReturn(pago);

        PagoDto resultado = service.save(nuevoPago);

        assertNotNull(resultado);
        verify(repository, times(1)).save(any(Pago.class));
    }

    @Test
    void findByEstado_retornaPagosConEseEstado() {
        when(repository.findByEstado("PENDIENTE")).thenReturn(List.of(pago));

        List<PagoDto> resultado = service.findByEstado("PENDIENTE");

        assertEquals(1, resultado.size());
        assertEquals("PENDIENTE", resultado.get(0).getEstado());
    }

    @Test
    void deleteById_inexistente_lanzaResourceNotFoundException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(99L));
    }
}
