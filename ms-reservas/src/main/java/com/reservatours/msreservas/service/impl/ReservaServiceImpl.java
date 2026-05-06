package com.reservatours.msreservas.service.impl;

import com.reservatours.msreservas.dto.ReservaDto;
import com.reservatours.msreservas.model.Reserva;
import com.reservatours.msreservas.repository.ReservaRepository;
import com.reservatours.msreservas.service.ReservaService;
import lombok.RequiredArgsConstructor;
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
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public ReservaDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public List<ReservaDto> findByFecha(String fecha) {
        return repository.findByFechaTour(LocalDate.parse(fecha))
                .stream().map(this::toDto).toList();
    }

    @Override
    public List<ReservaDto> findByTelefono(String telefono) {
        return repository.findByClienteTelefono(telefono)
                .stream().map(this::toDto).toList();
    }

    @Override
    public ReservaDto save(ReservaDto dto) {
        if (dto.getFechaCreacion() == null) {
            dto.setFechaCreacion(LocalDateTime.now());
        }
        if (dto.getEstado() == null) {
            dto.setEstado("CONFIRMADA");
        }
        if (dto.getNotificacionEnviada() == null) {
            dto.setNotificacionEnviada(false);
        }
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

    // Se ejecuta todos los dias a las 13:00 hora de Brasil (UTC-3 = 16:00 UTC)
    @Scheduled(cron = "0 0 16 * * *")
    @Override
    public void enviarNotificacionesDiaSiguiente() {
        LocalDate manana = LocalDate.now().plusDays(1);
        List<Reserva> reservas = repository
                .findByFechaTourAndNotificacionEnviada(manana, false);

        for (Reserva reserva : reservas) {
            enviarMensajeWhatsApp(reserva);
            reserva.setNotificacionEnviada(true);
            repository.save(reserva);
        }
    }

    private void enviarMensajeWhatsApp(Reserva reserva) {
        String url = "https://graph.facebook.com/v25.0/" + phoneNumberId + "/messages";

        String mensaje = String.format(
            "{\"messaging_product\":\"whatsapp\",\"to\":\"%s\",\"type\":\"text\",\"text\":{\"body\":\"Hola %s! Le recordamos que su tour *%s* es mañana.\\n\\n🕐 Hora de embarque: *%s*\\n📍 Punto de encuentro: *%s*\\n👤 Guía: *%s*\\n\\nCualquier consulta estamos disponibles. ¡Hasta mañana!\"}}",
            reserva.getClienteTelefono(),
            reserva.getClienteNombre(),
            reserva.getTourNombre(),
            reserva.getHoraEmbarque(),
            reserva.getPuntoEncuentro(),
            reserva.getNombreGuia()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(whatsappToken);

        HttpEntity<String> entity = new HttpEntity<>(mensaje, headers);
        try {
            restTemplate.postForEntity(url, entity, String.class);
        } catch (Exception e) {
            System.out.println("Error enviando WhatsApp: " + e.getMessage());
        }
    }
}
