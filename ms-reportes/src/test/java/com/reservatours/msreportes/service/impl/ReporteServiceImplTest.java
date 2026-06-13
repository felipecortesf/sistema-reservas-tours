package com.reservatours.msreportes.service.impl;

import com.reservatours.msreportes.dto.ReporteDto;
import com.reservatours.msreportes.model.Reporte;
import com.reservatours.msreportes.repository.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceImplTest {

    @Mock
    private ReporteRepository repository;

    @InjectMocks
    private ReporteServiceImpl service;

    private Reporte reporte;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "whatsappToken", "fake-token");
        ReflectionTestUtils.setField(service, "phoneNumberId", "123456");
        ReflectionTestUtils.setField(service, "telefonoFelipe", "56911111111");

        reporte = new Reporte(1L, "DIARIO", LocalDate.now(), 5, 3, 1, 0,
                "Reporte de hoy - Sistema funcionando correctamente.", false, LocalDateTime.now());
    }

    @Test
    void findById_existente_retornaReporteDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(reporte));

        ReporteDto resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals("DIARIO", resultado.getTipo());
    }

    @Test
    void findById_inexistente_retornaNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertNull(service.findById(99L));
    }

    @Test
    void findByFecha_existente_retornaReporteDto() {
        when(repository.findByFechaReporte(LocalDate.now())).thenReturn(Optional.of(reporte));

        ReporteDto resultado = service.findByFecha(LocalDate.now().toString());

        assertNotNull(resultado);
        assertEquals(5, resultado.getTotalReservas());
    }

    @Test
    void findByFecha_inexistente_retornaNull() {
        when(repository.findByFechaReporte(any(LocalDate.class))).thenReturn(Optional.empty());

        assertNull(service.findByFecha("2026-01-01"));
    }

    @Test
    void generarReporteDiario_creaReporteConTipoDiarioYFechaHoy() {
        when(repository.save(any(Reporte.class))).thenAnswer(i -> {
            Reporte r = (Reporte) i.getArgument(0);
            r.setId(1L);
            return r;
        });

        ReporteDto resultado = service.generarReporteDiario();

        assertEquals("DIARIO", resultado.getTipo());
        assertEquals(LocalDate.now(), resultado.getFechaReporte());
        assertFalse(resultado.getEnviadoWhatsapp());
        verify(repository, times(1)).save(any(Reporte.class));
    }

    @Test
    void enviarReporteDiarioWhatsApp_generaReporteYNoLanzaExcepcion() {
        when(repository.save(any(Reporte.class))).thenAnswer(i -> {
            Reporte r = (Reporte) i.getArgument(0);
            r.setId(1L);
            return r;
        });

        assertDoesNotThrow(() -> service.enviarReporteDiarioWhatsApp());

        verify(repository, times(1)).save(any(Reporte.class));
    }

    @Test
    void findAll_retornaListaDeReportes() {
        when(repository.findAll()).thenReturn(java.util.List.of(reporte));

        var resultado = service.findAll();

        assertEquals(1, resultado.size());
    }
}
