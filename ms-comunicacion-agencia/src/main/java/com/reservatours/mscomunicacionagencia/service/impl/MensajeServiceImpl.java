package com.reservatours.mscomunicacionagencia.service.impl;

import com.reservatours.mscomunicacionagencia.dto.MensajeDto;
import com.reservatours.mscomunicacionagencia.model.Mensaje;
import com.reservatours.mscomunicacionagencia.repository.MensajeRepository;
import com.reservatours.mscomunicacionagencia.service.MensajeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MensajeServiceImpl implements MensajeService {

    private static final Logger log = LoggerFactory.getLogger(MensajeServiceImpl.class);
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

    @Override
    public List<MensajeDto> findAll() {
        log.info("Consultando todos los mensajes");
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public MensajeDto findById(Long id) {
        log.info("Buscando mensaje con id: {}", id);
        return repository.findById(id).map(this::toDto).orElseGet(() -> {
            log.warn("Mensaje no encontrado con id: {}", id);
            return null;
        });
    }

    @Override
    public List<MensajeDto> findByReservaId(Long reservaId) {
        log.info("Buscando mensajes para reserva: {}", reservaId);
        return repository.findByReservaId(reservaId).stream().map(this::toDto).toList();
    }

    @Override
    public MensajeDto clientePreguntaKary(Long reservaId, String telefono, String mensaje) {
        log.info("Cliente {} pregunta a Kary sobre reserva: {}", telefono, reservaId);
        String mensajeKary = String.format(
            "Cliente pregunta (Reserva #%d):\\n%s\\nTelefono: %s",
            reservaId, mensaje, telefono);
        enviarWhatsApp(telefonoKary, mensajeKary);
        Mensaje m = new Mensaje(null, "CLIENTE", "KARY", telefono,
                telefonoKary, mensaje, "CONSULTA", "ENVIADO",
                reservaId, LocalDateTime.now());
        MensajeDto saved = toDto(repository.save(m));
        log.info("Pregunta de cliente enviada a Kary para reserva: {}", reservaId);
        return saved;
    }

    @Override
    public MensajeDto karyRespondeCliente(Long reservaId, String mensaje) {
        log.info("Kary responde a cliente sobre reserva: {}", reservaId);
        Mensaje m = new Mensaje(null, "KARY", "CLIENTE", telefonoKary,
                null, mensaje, "RESPUESTA", "ENVIADO",
                reservaId, LocalDateTime.now());
        return toDto(repository.save(m));
    }

    @Override
    public MensajeDto karyAlertaFelipe(Long reservaId, String mensaje) {
        log.warn("Kary alerta a Felipe sobre reserva: {} - {}", reservaId, mensaje);
        String mensajeFelipe = String.format("ALERTA Kary (Reserva #%d):\\n%s", reservaId, mensaje);
        enviarWhatsApp(telefonoFelipe, mensajeFelipe);
        Mensaje m = new Mensaje(null, "KARY", "FELIPE", telefonoKary,
                telefonoFelipe, mensaje, "ALERTA", "ENVIADO",
                reservaId, LocalDateTime.now());
        MensajeDto saved = toDto(repository.save(m));
        log.info("Alerta enviada a Felipe para reserva: {}", reservaId);
        return saved;
    }

    @Override
    public MensajeDto felipeLlamaCliente(Long reservaId, String mensaje) {
        log.info("Felipe llama a cliente sobre reserva: {}", reservaId);
        Mensaje m = new Mensaje(null, "FELIPE", "CLIENTE", telefonoFelipe,
                null, mensaje, "LLAMADA", "ENVIADO",
                reservaId, LocalDateTime.now());
        return toDto(repository.save(m));
    }

    private void enviarWhatsApp(String telefono, String mensaje) {
        log.info("Enviando WhatsApp a: {}", telefono);
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
            log.info("WhatsApp enviado exitosamente a: {}", telefono);
        } catch (Exception e) {
            log.error("Error enviando WhatsApp a {}: {}", telefono, e.getMessage());
        }
    }
}
