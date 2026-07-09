package com.reservatours.msreportes.controller;

import com.reservatours.msreportes.dto.ReporteDto;
import com.reservatours.msreportes.service.ReporteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteControllerTest {

    @Mock
    private ReporteService service;

    @InjectMocks
    private ReporteController controller;

    private ReporteDto reporteDto;

    @BeforeEach
    void setUp() {
        reporteDto = new ReporteDto(1L, "diario", LocalDate.of(2026, 7, 9), 20, 15, 2, 1,
                "Resumen de operacion del dia", false,
                LocalDateTime.of(2026, 7, 9, 23, 0));
    }

    @Test
    void findAll_retornaListaConStatus200() {
        when(service.findAll()).thenReturn(List.of(reporteDto));

        ResponseEntity<List<ReporteDto>> response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findById_existente_retorna200() {
        when(service.findById(1L)).thenReturn(reporteDto);

        ResponseEntity<ReporteDto> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("diario", response.getBody().getTipo());
    }

    @Test
    void findById_inexistente_retorna404() {
        when(service.findById(99L)).thenReturn(null);

        ResponseEntity<ReporteDto> response = controller.findById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findByFecha_retorna200() {
        when(service.findByFecha("2026-07-09")).thenReturn(reporteDto);

        ResponseEntity<ReporteDto> response = controller.findByFecha("2026-07-09");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reporteDto, response.getBody());
        verify(service, times(1)).findByFecha("2026-07-09");
    }

    @Test
    void generarReporte_retorna200ConReporteGenerado() {
        when(service.generarReporteDiario()).thenReturn(reporteDto);

        ResponseEntity<ReporteDto> response = controller.generarReporte();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(service, times(1)).generarReporteDiario();
    }

    @Test
    void enviarWhatsApp_retorna200ConMensajeConfirmacion() {
        doNothing().when(service).enviarReporteDiarioWhatsApp();

        ResponseEntity<String> response = controller.enviarWhatsApp();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Reporte enviado a WhatsApp", response.getBody());
        verify(service, times(1)).enviarReporteDiarioWhatsApp();
    }
}
