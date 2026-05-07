package com.reservatours.msnotificaciones.service.impl;

import com.reservatours.msnotificaciones.dto.NotificacionDto;
import com.reservatours.msnotificaciones.model.Notificacion;
import com.reservatours.msnotificaciones.repository.NotificacionRepository;
import com.reservatours.msnotificaciones.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final NotificacionRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${whatsapp.token}")
    private String whatsappToken;

    @Value("${whatsapp.phone.number.id}")
    private String phoneNumberId;

    private NotificacionDto toDto(Notificacion n) {
        return new NotificacionDto(n.getId(), n.getDestinatarioTelefono(),
                n.getDestinatarioNombre(), n.getMensaje(), n.getTipo(),
                n.getEstado(), n.getFechaEnvio(), n.getReservaId());
    }

    private Notificacion toEntity(NotificacionDto dto) {
        return new Notificacion(dto.getId(), dto.getDestinatarioTelefono(),
                dto.getDestinatarioNombre(), dto.getMensaje(), dto.getTipo(),
                dto.getEstado(), dto.getFechaEnvio(), dto.getReservaId());
    }

    @Override
    public List<NotificacionDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public NotificacionDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public List<NotificacionDto> findByTelefono(String telefono) {
        return repository.findByDestinatarioTelefono(telefono)
                .stream().map(this::toDto).toList();
    }

    @Override
    public List<NotificacionDto> findByReservaId(Long reservaId) {
        return repository.findByReservaId(reservaId)
                .stream().map(this::toDto).toList();
    }

    @Override
    public NotificacionDto enviarNotificacion(NotificacionDto dto) {
        String url = "https://graph.facebook.com/v25.0/" + phoneNumberId + "/messages";

        String mensaje = String.format(
            "{\"messaging_product\":\"whatsapp\",\"to\":\"%s\",\"type\":\"text\",\"text\":{\"body\":\"%s\"}}",
            dto.getDestinatarioTelefono(),
            dto.getMensaje()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(whatsappToken);

        HttpEntity<String> entity = new HttpEntity<>(mensaje, headers);
        try {
            restTemplate.postForEntity(url, entity, String.class);
            dto.setEstado("ENVIADO");
        } catch (Exception e) {
            dto.setEstado("ERROR");
            System.out.println("Error enviando WhatsApp: " + e.getMessage());
        }

        dto.setFechaEnvio(LocalDateTime.now());
        return toDto(repository.save(toEntity(dto)));
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
