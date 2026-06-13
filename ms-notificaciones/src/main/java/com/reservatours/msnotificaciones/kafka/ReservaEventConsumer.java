package com.reservatours.msnotificaciones.kafka;

import com.reservatours.msnotificaciones.model.Notificacion;
import com.reservatours.msnotificaciones.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class ReservaEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(ReservaEventConsumer.class);
    private final NotificacionRepository repository;

    public ReservaEventConsumer(NotificacionRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "reserva.creada", groupId = "ms-notificaciones")
    public void escucharReservaCreada(ReservaEvent evento) {
        log.info("Evento recibido de Kafka - reserva.creada - id: {}", evento.getId());

        Notificacion notificacion = new Notificacion();
        notificacion.setDestinatarioTelefono(evento.getClienteTelefono());
        notificacion.setDestinatarioNombre(evento.getClienteNombre());
        notificacion.setMensaje("Su reserva para " + evento.getTourNombre() +
                " ha sido confirmada. Punto de encuentro: " + evento.getPuntoEncuentro());
        notificacion.setTipo("WHATSAPP");
        notificacion.setEstado("PENDIENTE");
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setReservaId(evento.getId());

        repository.save(notificacion);
        log.info("Notificacion creada automaticamente para reserva id: {}", evento.getId());
    }
}
