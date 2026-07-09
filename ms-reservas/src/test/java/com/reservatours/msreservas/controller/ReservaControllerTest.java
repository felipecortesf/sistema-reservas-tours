package com.reservatours.msreservas.controller;

import com.reservatours.msreservas.dto.ReservaDto;
import com.reservatours.msreservas.service.ReservaService;
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
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaControllerTest {

    @Mock
    private ReservaService service;

    @InjectMocks
    private ReservaController controller;

    private ReservaDto reservaDto;

    @BeforeEach
    void setUp() {
        reservaDto = new ReservaDto(1L, "Juan Perez", "56912345678", "juan@email.com",
                1L, "Cristo Redentor", LocalDate.of(2026, 8, 1), LocalTime.of(7, 0),
                "Hotel Test", "Guia Test", "CONFIRMADA", false, LocalDateTime.now());
    }

    @Test
    void findAll_retorna200() {
        when(service.findAll()).thenReturn(List.of(reservaDto));

        ResponseEntity<List<ReservaDto>> response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findById_existente_retorna200() {
        when(service.findById(1L)).thenReturn(reservaDto);

        ResponseEntity<ReservaDto> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Juan Perez", response.getBody().getClienteNombre());
    }

    @Test
    void findById_inexistente_retorna404() {
        when(service.findById(99L)).thenReturn(null);

        ResponseEntity<ReservaDto> response = controller.findById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findByFecha_retorna200() {
        when(service.findByFecha("2026-08-01")).thenReturn(List.of(reservaDto));

        ResponseEntity<List<ReservaDto>> response = controller.findByFecha("2026-08-01");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findByTelefono_retorna200() {
        when(service.findByTelefono("56912345678")).thenReturn(List.of(reservaDto));

        ResponseEntity<List<ReservaDto>> response = controller.findByTelefono("56912345678");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void save_retorna201Created() {
        when(service.save(any(ReservaDto.class))).thenReturn(reservaDto);

        ResponseEntity<ReservaDto> response = controller.save(reservaDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteById_retorna200() {
        when(service.deleteById(1L)).thenReturn(true);

        ResponseEntity<Boolean> response = controller.deleteById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void notificar_disparaEnvioYRetorna200() {
        doNothing().when(service).enviarNotificacionesDiaSiguiente();

        ResponseEntity<String> response = controller.notificar();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).enviarNotificacionesDiaSiguiente();
    }

    @Test
    void contarConfirmadasPorTour_retorna200() {
        when(service.contarConfirmadasPorTour(1L)).thenReturn(5L);

        ResponseEntity<Long> response = controller.contarConfirmadasPorTour(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
    }

    @Test
    void findReservasProximas_retorna200() {
        when(service.findReservasProximas(7)).thenReturn(List.of(reservaDto));

        ResponseEntity<List<ReservaDto>> response = controller.findReservasProximas(7);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
