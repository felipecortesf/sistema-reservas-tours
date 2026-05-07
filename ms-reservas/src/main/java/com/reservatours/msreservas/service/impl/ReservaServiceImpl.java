package com.reservatours.msreservas.service.impl;

import com.reservatours.msreservas.dto.ReservaDto;
import com.reservatours.msreservas.model.Reserva;
import com.reservatours.msreservas.repository.ReservaRepository;
import com.reservatours.msreservas.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private static final Logger log = LoggerFactory.getLogger(ReservaServiceImpl.class);
    private final ReservaRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${whatsapp.token}")
    private String whatsappToken;

    @Value("${whatsapp.phone.number.id}")
    private String phoneNumberId;

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
    public List<ReservaDto> findAll() {
        log.info("Consultando todas las reservas");
        List<ReservaDto> reservas = repository.findAll().stream().map(this::toDto).toList();
        log.info("Total reservas encontradas: {}", reservas.size());
        return reservas;
    }

    @Override
    public ReservaDto findById(Long id) {
        log.info("Buscando reserva con id: {}", id);
        return repository.findById(id).map(r -> {
            log.info("Reserva encontrada para cliente: {}", r.getClienteNombre());
            return toDto(r);
        }).orElseGet(() -> {
            log.warn("Reserva no encontrada con id: {}", id);
            return null;
        });
    }

    @Override
    public List<ReservaDto> findByFecha(String fecha) {
        log.info("Buscando reservas para fecha: {}", fecha);
        List<ReservaDto> reservas = repository.findByFechaTour(LocalDate.parse(fecha))
                .stream().map(this::toDto).toList();
        log.info("Reservas encontradas para {}: {}", fecha, reservas.size());
        return reservas;
    }

    @Override
    public List<ReservaDto> findByTelefono(String telefono) {
        log.info("Buscando reservas para telefono: {}", telefono);
        return repository.findByClienteTelefono(telefono)
                .stream().map(this::toDto).toList();
    }

    @Override
    public ReservaDto save(ReservaDto dto) {
        log.info("Guardando reserva para cliente: {}", dto.getClienteNombre());
        try {
            if (dto.getFechaCreacion() == null) dto.setFechaCreacion(LocalDateTime.now());
            if (dto.getEstado() == null) dto.setEstado("CONFIRMADA");
            if (dto.getNotificacionEnviada() == null) dto.setNotificacionEnviada(false);
            ReservaDto saved = toDto(repository.save(toEntity(dto)));
            log.info("Reserva guardada exitosamente con id: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Error al guardar reserva: {}", e.getMessage());
            throw new RuntimeException("Error al guardar reserva: " + e.getMessage());
        }
    }

    @Override
    public Boolean deleteById(Long id) {
        log.info("Eliminando reserva con id: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Reserva eliminada exitosamente con id: {}", id);
            return true;
        }
        log.warn("No se encontro reserva para eliminar con id: {}", id);
        return false;
    }

    @Scheduled(cron = "0 0 16 * * *")
    @Override
    public void enviarNotificacionesDiaSiguiente() {
        log.info("Iniciando envio de notificaciones para manana");
        LocalDate manana = LocalDate.now().plusDays(1);
        List<Reserva> reservas = repository.findByFechaTourAndNotificacionEnviada(manana, false);
        log.info("Reservas a notificar: {}", reservas.size());
        for (Reserva reserva : reservas) {
            enviarMensajeWhatsApp(reserva);
            reserva.setNotificacionEnviada(true);
            repository.save(reserva);
            log.info("Notificacion enviada a: {}", reserva.getClienteTelefono());
        }
    }

    private void enviarMensajeWhatsApp(Reserva reserva) {
        String url = "https://graph.facebook.com/v25.0/" + phoneNumberId + "/messages";
        String mensaje = String.format(
            "{\"messaging_product\":\"whatsapp\",\"to\":\"%s\",\"type\":\"text\",\"text\":{\"body\":\"Hola %s! Le recordamos que su tour *%s* es mañana.\\n\\n🕐 Hora de embarque: *%s*\\n📍 Punto de encuentro: *%s*\\n👤 Guía: *%s*\\n\\nCualquier consulta estamos disponibles. ¡Hasta mañana!\"}}",
            reserva.getClienteTelefono(), reserva.getClienteNombre(),
            reserva.getTourNombre(), reserva.getHoraEmbarque(),
            reserva.getPuntoEncuentro(), reserva.getNombreGuia()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(whatsappToken);
        try {
            restTemplate.postForEntity(url, new HttpEntity<>(mensaje, headers), String.class);
            log.info("WhatsApp enviado a: {}", reserva.getClienteTelefono());
        } catch (Exception e) {
            log.error("Error enviando WhatsApp a {}: {}", reserva.getClienteTelefono(), e.getMessage());
        }
    }
}
