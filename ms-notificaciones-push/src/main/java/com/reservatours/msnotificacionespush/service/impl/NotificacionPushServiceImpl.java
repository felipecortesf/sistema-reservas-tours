package com.reservatours.msnotificacionespush.service.impl;

import com.reservatours.msnotificacionespush.dto.NotificacionPushDto;
import com.reservatours.msnotificacionespush.model.NotificacionPush;
import com.reservatours.msnotificacionespush.repository.NotificacionPushRepository;
import com.reservatours.msnotificacionespush.service.NotificacionPushService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionPushServiceImpl implements NotificacionPushService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionPushServiceImpl.class);
    private final NotificacionPushRepository repository;

    private NotificacionPushDto toDto(NotificacionPush n) {
        return new NotificacionPushDto(n.getId(), n.getTitulo(), n.getMensaje(),
                n.getDestinatarioId(), n.getDestinatarioTelefono(),
                n.getTipo(), n.getEstado(), n.getFechaEnvio(), n.getLeida());
    }

    private NotificacionPush toEntity(NotificacionPushDto dto) {
        return new NotificacionPush(dto.getId(), dto.getTitulo(), dto.getMensaje(),
                dto.getDestinatarioId(), dto.getDestinatarioTelefono(),
                dto.getTipo(), dto.getEstado(), dto.getFechaEnvio(), dto.getLeida());
    }

    @Override
    public List<NotificacionPushDto> findAll() {
        log.info("Consultando todas las notificaciones push");
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public NotificacionPushDto findById(Long id) {
        log.info("Buscando notificacion push con id: {}", id);
        return repository.findById(id).map(this::toDto).orElseGet(() -> {
            log.warn("Notificacion push no encontrada con id: {}", id);
            return null;
        });
    }

    @Override
    public List<NotificacionPushDto> findByTelefono(String telefono) {
        log.info("Buscando notificaciones push para telefono: {}", telefono);
        return repository.findByDestinatarioTelefono(telefono).stream().map(this::toDto).toList();
    }

    @Override
    public List<NotificacionPushDto> findNoLeidas(String telefono) {
        log.info("Buscando notificaciones no leidas para: {}", telefono);
        return repository.findByLeida(false).stream()
                .filter(n -> telefono.equals(n.getDestinatarioTelefono()))
                .map(this::toDto).toList();
    }

    @Override
    public NotificacionPushDto enviar(NotificacionPushDto dto) {
        log.info("Enviando notificacion push: {} a {}", dto.getTitulo(), dto.getDestinatarioTelefono());
        dto.setEstado("ENVIADO");
        dto.setFechaEnvio(LocalDateTime.now());
        dto.setLeida(false);
        NotificacionPushDto saved = toDto(repository.save(toEntity(dto)));
        log.info("Notificacion push enviada con id: {}", saved.getId());
        return saved;
    }

    @Override
    public NotificacionPushDto marcarLeida(Long id) {
        log.info("Marcando como leida notificacion push id: {}", id);
        return repository.findById(id).map(n -> {
            n.setLeida(true);
            log.info("Notificacion push marcada como leida: {}", id);
            return toDto(repository.save(n));
        }).orElseGet(() -> {
            log.warn("Notificacion push no encontrada con id: {}", id);
            return null;
        });
    }

    @Override
    public Boolean deleteById(Long id) {
        log.info("Eliminando notificacion push con id: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Notificacion push eliminada con id: {}", id);
            return true;
        }
        log.warn("Notificacion push no encontrada con id: {}", id);
        return false;
    }
}
