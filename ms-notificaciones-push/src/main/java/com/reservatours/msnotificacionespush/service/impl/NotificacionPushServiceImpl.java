package com.reservatours.msnotificacionespush.service.impl;

import com.reservatours.msnotificacionespush.dto.NotificacionPushDto;
import com.reservatours.msnotificacionespush.model.NotificacionPush;
import com.reservatours.msnotificacionespush.repository.NotificacionPushRepository;
import com.reservatours.msnotificacionespush.service.NotificacionPushService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionPushServiceImpl implements NotificacionPushService {

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
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public NotificacionPushDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public List<NotificacionPushDto> findByTelefono(String telefono) {
        return repository.findByDestinatarioTelefono(telefono)
                .stream().map(this::toDto).toList();
    }

    @Override
    public List<NotificacionPushDto> findNoLeidas(String telefono) {
        return repository.findByLeida(false)
                .stream()
                .filter(n -> telefono.equals(n.getDestinatarioTelefono()))
                .map(this::toDto).toList();
    }

    @Override
    public NotificacionPushDto enviar(NotificacionPushDto dto) {
        dto.setEstado("ENVIADO");
        dto.setFechaEnvio(LocalDateTime.now());
        dto.setLeida(false);
        return toDto(repository.save(toEntity(dto)));
    }

    @Override
    public NotificacionPushDto marcarLeida(Long id) {
        return repository.findById(id).map(n -> {
            n.setLeida(true);
            return toDto(repository.save(n));
        }).orElse(null);
    }

    @Override
    public Boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
