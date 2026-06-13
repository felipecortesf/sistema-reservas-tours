package com.reservatours.msreservas.service.impl;

import com.reservatours.msreservas.dto.ReservaDto;
import com.reservatours.msreservas.model.Reserva;
import com.reservatours.msreservas.repository.ReservaRepository;
import com.reservatours.msreservas.service.ReservaService;
import com.reservatours.msreservas.exception.ResourceNotFoundException;
import com.reservatours.msreservas.client.WhatsappClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private static final Logger log = LoggerFactory.getLogger(ReservaServiceImpl.class);
    private final ReservaRepository repository;
    private final com.reservatours.msreservas.kafka.ReservaEventProducer eventProducer;
    private final WhatsappClient whatsappClient;

    private ReservaDto toDto(Reserva r) {
        return new ReservaDto(r.getId(), r.getClienteNombre(), r.getClienteTelefono(),
                r.getClienteEmail(), r.getTourId(), r.getTourNombre(),
                r.getFechaTour(), r.getHoraEmbarque(), r.getPuntoEncuentro(),
                r.getNombreGuia(), r.getEstado(), r.getNotificacionEnviada(),
                r.getFechaCreacion());
    }

    private Reserva toEntity(ReservaDto dto) {
        return new Reserva(dto.getId(), dto.getClienteNombre(), dto.getClienteTelefono(),
                dto.getClienteEmail(), dto.getTourId(), dto.getTourNombre(),
                dto.getFechaTour(), dto.getHoraEmbarque(), dto.getPuntoEncuentro(),
                dto.getNombreGuia(), dto.getEstado(), dto.getNotificacionEnviada(),
                dto.getFechaCreacion());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDto> findAll() {
        log.info("Consultando todas las reservas");
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaDto findById(Long id) {
        log.info("Buscando reserva con id: {}", id);
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDto> findByFecha(String fecha) {
        log.info("Buscando reservas para fecha: {}", fecha);
        return repository.findByFechaTour(LocalDate.parse(fecha))
                .stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservaDto> findByTelefono(String telefono) {
        log.info("Buscando reservas para telefono: {}", telefono);
        return repository.findByClienteTelefono(telefono)
                .stream().map(this::toDto).toList();
    }

    @Override
    @Transactional
    public ReservaDto save(ReservaDto dto) {
        log.info("Guardando reserva para cliente: {}", dto.getClienteNombre());
        try {
            if (dto.getFechaCreacion() == null) dto.setFechaCreacion(LocalDateTime.now());
            if (dto.getEstado() == null) dto.setEstado("CONFIRMADA");
            if (dto.getNotificacionEnviada() == null) dto.setNotificacionEnviada(false);
            ReservaDto saved = toDto(repository.save(toEntity(dto)));
            eventProducer.publicarReservaCreada(saved);
            return saved;
        } catch (Exception e) {
            log.error("Error al guardar reserva: {}", e.getMessage());
            throw new RuntimeException("Error al guardar reserva: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Boolean deleteById(Long id) {
        log.info("Eliminando reserva con id: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        throw new ResourceNotFoundException("No se encontró reserva para eliminar con ID: " + id);
    }

    @Scheduled(cron = "0 0 16 * * *")
    @Override
    @Transactional
    public void enviarNotificacionesDiaSiguiente() {
        log.info("Iniciando envio de notificaciones para mañana");
        LocalDate manana = LocalDate.now().plusDays(1);
        List<Reserva> reservas = repository.findByFechaTourAndNotificacionEnviada(manana, false);
        log.info("Reservas a notificar: {}", reservas.size());
        for (Reserva reserva : reservas) {
            enviarMensajeWhatsApp(reserva);
            reserva.setNotificacionEnviada(true);
            repository.save(reserva);
        }
    }

    private void enviarMensajeWhatsApp(Reserva reserva) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("telefono", reserva.getClienteTelefono());
        payload.put("nombre", reserva.getClienteNombre());
        payload.put("tour", reserva.getTourNombre());
        payload.put("hora", reserva.getHoraEmbarque());
        payload.put("puntoEncuentro", reserva.getPuntoEncuentro());
        payload.put("guia", reserva.getNombreGuia());

        try {
            log.info("Enviando peticion remota via OpenFeign a ms-whatsapp para el cliente: {}", reserva.getClienteNombre());
            whatsappClient.enviarMensaje(payload);
            log.info("Notificacion remota procesada exitosamente por ms-whatsapp");
        } catch (Exception e) {
            log.error("Error en la comunicacion remota con ms-whatsapp para {}: {}", reserva.getClienteTelefono(), e.getMessage());
        }
    }
}
