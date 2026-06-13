package com.reservatours.msembarques.service.impl;

import com.reservatours.msembarques.dto.EmbarqueDto;
import com.reservatours.msembarques.exception.ResourceNotFoundException;
import com.reservatours.msembarques.model.Embarque;
import com.reservatours.msembarques.repository.EmbarqueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmbarqueServiceImplTest {

    @Mock
    private EmbarqueRepository repository;

    @InjectMocks
    private EmbarqueServiceImpl service;

    private Embarque embarque;

    @BeforeEach
    void setUp() {
        embarque = new Embarque(1L, 1L, "Cristo Redentor", LocalDate.of(2026, 8, 1),
                LocalTime.of(7, 0), null, "Hotel Test", "Guia Test", "56911111111",
                "PROGRAMADO", null);
    }

    @Test
    void findById_existente_retornaEmbarqueDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(embarque));

        EmbarqueDto resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals("PROGRAMADO", resultado.getEstado());
    }

    @Test
    void findById_inexistente_lanzaResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void save_sinEstado_asignaProgramadoPorDefecto() {
        EmbarqueDto nuevo = new EmbarqueDto(null, 2L, "Pao de Acucar", LocalDate.of(2026, 9, 1),
                LocalTime.of(8, 0), null, "Hotel Ipanema", "Guia Ana", "56922222222", null, null);

        when(repository.save(any(Embarque.class))).thenReturn(embarque);

        EmbarqueDto resultado = service.save(nuevo);

        assertEquals("PROGRAMADO", resultado.getEstado());
    }

    @Test
    void reportarRetraso_existente_cambiaEstadoARetrasado() {
        when(repository.findById(1L)).thenReturn(Optional.of(embarque));
        when(repository.save(any(Embarque.class))).thenAnswer(i -> i.getArgument(0));

        EmbarqueDto resultado = service.reportarRetraso(1L, "07:30:00", "Trafico en la ruta");

        assertEquals("RETRASADO", resultado.getEstado());
        assertEquals(LocalTime.of(7, 30), resultado.getHoraEmbarqueReal());
        assertEquals("Trafico en la ruta", resultado.getObservaciones());
    }

    @Test
    void reportarRetraso_inexistente_lanzaResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.reportarRetraso(99L, "08:00:00", "obs"));
    }

    @Test
    void actualizarEstado_existente_actualizaCorrectamente() {
        when(repository.findById(1L)).thenReturn(Optional.of(embarque));
        when(repository.save(any(Embarque.class))).thenAnswer(i -> i.getArgument(0));

        EmbarqueDto resultado = service.actualizarEstado(1L, "COMPLETADO", "Sin novedades");

        assertEquals("COMPLETADO", resultado.getEstado());
        assertEquals("Sin novedades", resultado.getObservaciones());
    }

    @Test
    void deleteById_inexistente_lanzaResourceNotFoundException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(99L));
    }
}
