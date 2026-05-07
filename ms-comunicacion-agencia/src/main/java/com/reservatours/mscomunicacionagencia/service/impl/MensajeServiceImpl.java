package com.reservatours.mscomunicacionagencia.service.impl;

import com.reservatours.mscomunicacionagencia.dto.MensajeDto;
import com.reservatours.mscomunicacionagencia.model.Mensaje;
import com.reservatours.mscomunicacionagencia.repository.MensajeRepository;
import com.reservatours.mscomunicacionagencia.service.MensajeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MensajeServiceImpl implements MensajeService {

    private final MensajeRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${whatsapp.token}")
    private String whatsappToken;

    @Value("${whatsapp.phone.number.id}")
    private String phoneNumberId;

    @Value("${telefono.felipe}")
    private String telefonoFelipe;

    @Value("${telefono.kary}")
    private String telefonoKary;

    private MensajeDto toDto(Mensaje m) {
        return new MensajeDto(m.getId(), m.getOrigen(), m.getDestino(),
                m.getTelefonoOrigen(), m.getTelefonoDestino(),
                m.getContenido(), m.getTipo(), m.getEstado(),
                m.getReservaId(), m.getFechaMensaje());
    }

    private Mensaje toEntity(MensajeDto dto) {
        return new Mensaje(dto.getId(), dto.getOrigen(), dto.getDestino(),
                dto.getTelefonoOrigen(), dto.getTelefonoDestino(),
                dto.getContenido(), dto.getTipo(), dto.getEstado(),
                dto.getReservaId(), dto.getFechaMensaje());
    }

    @Override
    public List<MensajeDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public MensajeDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public List<MensajeDto> findByReservaId(Long reservaId) {
        return repository.findByReservaId(reservaId)
                .stream().map(this::toDto).toList();
    }

    @Override
    public MensajeDto clientePreguntaKary(Long reservaId, String telefono, String mensaje) {
        // Reenviar pregunta del cliente a Kary via WhatsApp
        String mensajeKary = String.format("📱 Cliente pregunta (Reserva #%d):\n%s\n\nTelefono cliente: %s",
                reservaId, mensaje, telefono);
        enviarWhatsApp(telefonoKary, mensajeKary);

        Mensaje m = new Mensaje(null, "CLIENTE", "KARY", telefono,
                telefonoKary, mensaje, "CONSULTA", "ENVIADO",
                reservaId, LocalDateTime.now());
        return toDto(repository.save(m));
    }

    @Override
    public MensajeDto karyRespondeCliente(Long reservaId, String mensaje) {
        // Buscar telefono del cliente por reservaId y reenviar respuesta
        String mensajeCliente = String.format("🚌 Respuesta sobre su tour:\n%s", mensaje);

        Mensaje m = new Mensaje(null, "KARY", "CLIENTE", telefonoKary,
                null, mensaje, "RESPUESTA", "ENVIADO",
                reservaId, LocalDateTime.now());
        return toDto(repository.save(m));
    }

    @Override
    public MensajeDto karyAlertaFelipe(Long reservaId, String mensaje) {
        // Kary avisa a Felipe que un cliente no aparece
        String mensajeFelipe = String.format("⚠️ ALERTA Kary (Reserva #%d):\n%s", reservaId, mensaje);
        enviarWhatsApp(telefonoFelipe, mensajeFelipe);

        Mensaje m = new Mensaje(null, "KARY", "FELIPE", telefonoKary,
                telefonoFelipe, mensaje, "ALERTA", "ENVIADO",
                reservaId, LocalDateTime.now());
        return toDto(repository.save(m));
    }

    @Override
    public MensajeDto felipeLlamaCliente(Long reservaId, String mensaje) {
        Mensaje m = new Mensaje(null, "FELIPE", "CLIENTE", telefonoFelipe,
                null, mensaje, "LLAMADA", "ENVIADO",
                reservaId, LocalDateTime.now());
        return toDto(repository.save(m));
    }

    private void enviarWhatsApp(String telefono, String mensaje) {
        String url = "https://graph.facebook.com/v25.0/" + phoneNumberId + "/messages";
        String body = String.format(
            "{\"messaging_product\":\"whatsapp\",\"to\":\"%s\",\"type\":\"text\",\"text\":{\"body\":\"%s\"}}",
            telefono, mensaje.replace("\"", "'").replace("\n", "\\n")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(whatsappToken);

        try {
            restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);
        } catch (Exception e) {
            System.out.println("Error WhatsApp: " + e.getMessage());
        }
    }
}
