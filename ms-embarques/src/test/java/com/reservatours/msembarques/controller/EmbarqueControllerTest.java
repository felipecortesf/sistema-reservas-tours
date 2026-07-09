package com.reservatours.msembarques.controller;

import com.reservatours.msembarques.dto.EmbarqueDto;
import com.reservatours.msembarques.service.EmbarqueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmbarqueControllerTest {

    @Mock
    private EmbarqueService service;

    @InjectMocks
    private EmbarqueController controller;

    private EmbarqueDto embarqueDto;

    @BeforeEach
    void setUp() {
        embarqueDto = new EmbarqueDto(1L, 1L, "Cristo Redentor", LocalDate.of(2026, 8, 1),
                LocalTime.of(7, 0), null, "Hotel Test", "Guia Test", "56911111111",
                "PROGRAMADO", null);
    }

    @Test
    void findAll_retornaListaConStatus200() {
        when(service.findAll()).thenReturn(List.of(embarqueDto));

        ResponseEntity<List<EmbarqueDto>> response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findById_existente_retorna200() {
        when(service.findById(1L)).thenReturn(embarqueDto);

        ResponseEntity<EmbarqueDto> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cristo Redentor", response.getBody().getTourNombre());
    }

    @Test
    void findById_inexistente_retorna404() {
        when(service.findById(99L)).thenReturn(null);

        ResponseEntity<EmbarqueDto> response = controller.findById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findByFecha_retorna200() {
        when(service.findByFecha(anyString())).thenReturn(List.of(embarqueDto));

        ResponseEntity<List<EmbarqueDto>> response = controller.findByFecha("2026-08-01");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findByEstado_retorna200() {
        when(service.findByEstado(anyString())).thenReturn(List.of(embarqueDto));

        ResponseEntity<List<EmbarqueDto>> response = controller.findByEstado("PROGRAMADO");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).findByEstado("PROGRAMADO");
    }

    @Test
    void save_retorna200ConEmbarqueCreado() {
        when(service.save(any(EmbarqueDto.class))).thenReturn(embarqueDto);

        ResponseEntity<EmbarqueDto> response = controller.save(embarqueDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void actualizarEstado_retorna200() {
        when(service.actualizarEstado(1L, "COMPLETADO", "Sin novedades")).thenReturn(embarqueDto);

        ResponseEntity<EmbarqueDto> response = controller.actualizarEstado(1L, "COMPLETADO", "Sin novedades");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).actualizarEstado(1L, "COMPLETADO", "Sin novedades");
    }

    @Test
    void reportarRetraso_retorna200() {
        when(service.reportarRetraso(1L, "07:30:00", "Trafico en la ruta")).thenReturn(embarqueDto);

        ResponseEntity<EmbarqueDto> response = controller.reportarRetraso(1L, "07:30:00", "Trafico en la ruta");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).reportarRetraso(1L, "07:30:00", "Trafico en la ruta");
    }

    @Test
    void deleteById_retorna200() {
        when(service.deleteById(1L)).thenReturn(true);

        ResponseEntity<Boolean> response = controller.deleteById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }
}
