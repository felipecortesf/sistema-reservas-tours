package com.reservatours.mscatalogotours.controller;

import com.reservatours.mscatalogotours.dto.TourDto;
import com.reservatours.mscatalogotours.service.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TourControllerTest {

    @Mock
    private TourService service;

    @InjectMocks
    private TourController controller;

    private TourDto tourDto;

    @BeforeEach
    void setUp() {
        tourDto = new TourDto(1L, "Cristo Redentor", "Vista panoramica", "Rio de Janeiro",
                new BigDecimal("47827.00"), 15, LocalTime.of(7, 0),
                LocalDate.of(2026, 8, 1), true);
    }

    @Test
    void findAll_retornaListaConStatus200() {
        when(service.findAll()).thenReturn(List.of(tourDto));

        ResponseEntity<List<TourDto>> response = controller.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findActivos_retornaListaConStatus200() {
        when(service.findActivos()).thenReturn(List.of(tourDto));

        ResponseEntity<List<TourDto>> response = controller.findActivos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).findActivos();
    }

    @Test
    void findById_existente_retorna200() {
        when(service.findById(1L)).thenReturn(tourDto);

        ResponseEntity<TourDto> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cristo Redentor", response.getBody().getNombre());
    }

    @Test
    void findById_inexistente_retorna404() {
        when(service.findById(99L)).thenReturn(null);

        ResponseEntity<TourDto> response = controller.findById(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void save_retorna200ConTourCreado() {
        when(service.save(any(TourDto.class))).thenReturn(tourDto);

        ResponseEntity<TourDto> response = controller.save(tourDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
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
    void reducirCupo_retorna200() {
        when(service.reducirCupo(1L)).thenReturn(tourDto);

        ResponseEntity<TourDto> response = controller.reducirCupo(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).reducirCupo(1L);
    }

    @Test
    void findToursOrdenadosPorPrecio_retorna200() {
        when(service.findToursOrdenadosPorPrecio()).thenReturn(List.of(tourDto));

        ResponseEntity<List<TourDto>> response = controller.findToursOrdenadosPorPrecio();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void precioPromedioPorDestino_retorna200() {
        when(service.precioPromedioPorDestino(anyString())).thenReturn(new BigDecimal("45000.00"));

        ResponseEntity<BigDecimal> response = controller.precioPromedioPorDestino("Rio de Janeiro");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new BigDecimal("45000.00"), response.getBody());
    }
}
