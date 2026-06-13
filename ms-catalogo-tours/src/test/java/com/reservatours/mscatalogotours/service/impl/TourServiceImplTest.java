package com.reservatours.mscatalogotours.service.impl;

import com.reservatours.mscatalogotours.dto.TourDto;
import com.reservatours.mscatalogotours.model.Tour;
import com.reservatours.mscatalogotours.repository.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TourServiceImplTest {

    @Mock
    private TourRepository repository;

    @InjectMocks
    private TourServiceImpl service;

    private Tour tour;

    @BeforeEach
    void setUp() {
        tour = new Tour(1L, "Cristo Redentor", "Vista panoramica", "Rio de Janeiro",
                new BigDecimal("47827.00"), 15, LocalTime.of(7, 0),
                LocalDate.of(2026, 8, 1), true);
    }

    @Test
    void findById_existente_retornaTourDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(tour));

        TourDto resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals("Cristo Redentor", resultado.getNombre());
    }

    @Test
    void findById_inexistente_retornaNull() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        TourDto resultado = service.findById(99L);

        assertNull(resultado);
    }

    @Test
    void findActivos_retornaSoloToursActivos() {
        when(repository.findByActivoTrue()).thenReturn(List.of(tour));

        List<TourDto> resultado = service.findActivos();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getActivo());
    }

    @Test
    void reducirCupo_conCuposDisponibles_disminuyeEnUno() {
        when(repository.findById(1L)).thenReturn(Optional.of(tour));
        when(repository.save(any(Tour.class))).thenAnswer(i -> i.getArgument(0));

        TourDto resultado = service.reducirCupo(1L);

        assertNotNull(resultado);
        assertEquals(14, resultado.getCuposDisponibles());
        verify(repository, times(1)).save(any(Tour.class));
    }

    @Test
    void reducirCupo_sinCuposDisponibles_retornaNullYNoGuarda() {
        tour.setCuposDisponibles(0);
        when(repository.findById(1L)).thenReturn(Optional.of(tour));

        TourDto resultado = service.reducirCupo(1L);

        assertNull(resultado);
        verify(repository, never()).save(any(Tour.class));
    }

    @Test
    void deleteById_existente_retornaTrue() {
        when(repository.existsById(1L)).thenReturn(true);

        Boolean resultado = service.deleteById(1L);

        assertTrue(resultado);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_inexistente_retornaFalse() {
        when(repository.existsById(99L)).thenReturn(false);

        Boolean resultado = service.deleteById(99L);

        assertFalse(resultado);
        verify(repository, never()).deleteById(any());
    }
}
