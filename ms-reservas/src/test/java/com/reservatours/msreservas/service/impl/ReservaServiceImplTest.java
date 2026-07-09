package com.reservatours.msreservas.service.impl;

import com.reservatours.msreservas.client.CatalogoClient;
import com.reservatours.msreservas.client.WhatsappClient;
import com.reservatours.msreservas.dto.ReservaDto;
import com.reservatours.msreservas.exception.ResourceNotFoundException;
import com.reservatours.msreservas.exception.TourNoDisponibleException;
import com.reservatours.msreservas.kafka.ReservaEventProducer;
import com.reservatours.msreservas.model.Reserva;
import com.reservatours.msreservas.repository.ReservaRepository;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository repository;

    @Mock
    private ReservaEventProducer eventProducer;

    @Mock
    private WhatsappClient whatsappClient;

    @Mock
    private CatalogoClient catalogoClient;

    @InjectMocks
    private ReservaServiceImpl service;

    private FeignException feignErrorWithStatus(int status, String path) {
        Request request = Request.create(Request.HttpMethod.PUT, path,
                Map.of(), null, StandardCharsets.UTF_8, new RequestTemplate());
        Response response = Response.builder()
                .status(status)
                .reason("error")
                .request(request)
                .headers(Map.of())
                .build();
        return FeignException.errorStatus("CatalogoClient#reducirCupo(Long)", response);
    }

    private Reserva reserva;
    private ReservaDto reservaDto;

    @BeforeEach
    void setUp() {
        reserva = new Reserva(1L, "Juan Perez", "56912345678", "juan@email.com",
                1L, "Cristo Redentor", LocalDate.of(2026, 8, 1), LocalTime.of(7, 0),
                "Hotel Test", "Guia Test", "CONFIRMADA", false, LocalDateTime.now());

        reservaDto = new ReservaDto(1L, "Juan Perez", "56912345678", "juan@email.com",
                1L, "Cristo Redentor", LocalDate.of(2026, 8, 1), LocalTime.of(7, 0),
                "Hotel Test", "Guia Test", "CONFIRMADA", false, LocalDateTime.now());
    }

    @Test
    void findById_existente_retornaReservaDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(reserva));

        ReservaDto resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getClienteNombre());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void findById_inexistente_lanzaResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
        verify(repository, times(1)).findById(99L);
    }

    @Test
    void save_reservaNueva_asignaEstadoConfirmadaPorDefecto() {
        ReservaDto nuevaReserva = new ReservaDto(null, "Maria Garcia", "56987654321",
                "maria@email.com", 2L, "Pao de Acucar", LocalDate.of(2026, 9, 1),
                LocalTime.of(8, 0), "Hotel Ipanema", "Guia Ana", null, null, null);

        when(repository.save(any(Reserva.class))).thenReturn(reserva);

        ReservaDto resultado = service.save(nuevaReserva);

        assertNotNull(resultado);
        assertEquals("CONFIRMADA", resultado.getEstado());
        verify(catalogoClient, times(1)).reducirCupo(2L);
        verify(eventProducer, times(1)).publicarReservaCreada(any(ReservaDto.class));
    }

    @Test
    void save_tourSinCupos_lanzaTourNoDisponibleExceptionYNoGuarda() {
        ReservaDto nuevaReserva = new ReservaDto(null, "Maria Garcia", "56987654321",
                "maria@email.com", 2L, "Pao de Acucar", LocalDate.of(2026, 9, 1),
                LocalTime.of(8, 0), "Hotel Ipanema", "Guia Ana", null, null, null);

        when(catalogoClient.reducirCupo(2L)).thenThrow(feignErrorWithStatus(409, "/api/v1/tours/2/reducir-cupo"));

        assertThrows(TourNoDisponibleException.class, () -> service.save(nuevaReserva));
        verify(repository, never()).save(any(Reserva.class));
        verify(eventProducer, never()).publicarReservaCreada(any(ReservaDto.class));
    }

    @Test
    void save_tourInexistente_lanzaResourceNotFoundExceptionYNoGuarda() {
        ReservaDto nuevaReserva = new ReservaDto(null, "Maria Garcia", "56987654321",
                "maria@email.com", 999L, "Tour Inexistente", LocalDate.of(2026, 9, 1),
                LocalTime.of(8, 0), "Hotel Ipanema", "Guia Ana", null, null, null);

        when(catalogoClient.reducirCupo(999L)).thenThrow(feignErrorWithStatus(404, "/api/v1/tours/999/reducir-cupo"));

        assertThrows(ResourceNotFoundException.class, () -> service.save(nuevaReserva));
        verify(repository, never()).save(any(Reserva.class));
        verify(eventProducer, never()).publicarReservaCreada(any(ReservaDto.class));
    }

    @Test
    void findAll_retornaListaDeReservas() {
        when(repository.findAll()).thenReturn(List.of(reserva));

        List<ReservaDto> resultado = service.findAll();

        assertEquals(1, resultado.size());
        assertEquals("Juan Perez", resultado.get(0).getClienteNombre());
    }

    @Test
    void deleteById_existente_retornaTrue() {
        when(repository.existsById(1L)).thenReturn(true);

        Boolean resultado = service.deleteById(1L);

        assertTrue(resultado);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_inexistente_lanzaResourceNotFoundException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(99L));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void findByFecha_retornaReservasDeEsaFecha() {
        when(repository.findByFechaTour(LocalDate.of(2026, 8, 1))).thenReturn(List.of(reserva));

        List<ReservaDto> resultado = service.findByFecha("2026-08-01");

        assertEquals(1, resultado.size());
        verify(repository, times(1)).findByFechaTour(LocalDate.of(2026, 8, 1));
    }
}
