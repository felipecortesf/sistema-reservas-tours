package com.reservatours.mswhatsapp.service.impl;

import com.reservatours.mswhatsapp.dto.MensajeWhatsappDto;
import com.reservatours.mswhatsapp.model.MensajeWhatsapp;
import com.reservatours.mswhatsapp.repository.MensajeWhatsappRepository;
import com.reservatours.mswhatsapp.service.WhatsappService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WhatsappServiceImpl implements WhatsappService {

    private static final Logger log = LoggerFactory.getLogger(WhatsappServiceImpl.class);
    private final MensajeWhatsappRepository repository;

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.whatsapp.from}")
    private String fromNumber;

    @Value("${telefono.kary}")
    private String telefonoKary;

    @Value("${whatsapp.verify.token}")
    private String verifyToken;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
        log.info("Twilio inicializado correctamente");
    }

    private MensajeWhatsappDto toDto(MensajeWhatsapp m) {
        return new MensajeWhatsappDto(m.getId(), m.getTelefonoRemitente(),
                m.getTelefonoDestinatario(), m.getNombreRemitente(),
                m.getContenido(), m.getTipoMensaje(), m.getDireccion(),
                m.getEstado(), m.getProcesado(), m.getFechaMensaje());
    }

    @Override
    public List<MensajeWhatsappDto> findAll() {
        log.info("Consultando todos los mensajes WhatsApp");
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public MensajeWhatsappDto findById(Long id) {
        log.info("Buscando mensaje WhatsApp con id: {}", id);
        return repository.findById(id).map(this::toDto).orElseGet(() -> {
            log.warn("Mensaje no encontrado con id: {}", id);
            return null;
        });
    }

    @Override
    public List<MensajeWhatsappDto> findNoProcessados() {
        log.info("Consultando mensajes no procesados");
        return repository.findByProcesado(false).stream().map(this::toDto).toList();
    }

    @Override
    public MensajeWhatsappDto enviarMensaje(String telefono, String mensaje) {
        log.info("Enviando mensaje WhatsApp via Twilio a: {}", telefono);
        String estado = "ENVIADO";
        try {
            Message message = Message.creator(
                new PhoneNumber("whatsapp:+" + telefono),
                new PhoneNumber(fromNumber),
                mensaje
            ).create();
            log.info("Mensaje enviado exitosamente. SID: {}", message.getSid());
        } catch (Exception e) {
            estado = "ERROR";
            log.error("Error enviando mensaje a {}: {}", telefono, e.getMessage());
        }
        MensajeWhatsapp m = new MensajeWhatsapp(null, fromNumber, "whatsapp:+" + telefono,
                "Sistema", mensaje, "TEXTO", "SALIENTE",
                estado, true, LocalDateTime.now());
        return toDto(repository.save(m));
    }

    @Override
    public MensajeWhatsappDto recibirMensaje(String telefono, String nombre, String mensaje) {
        log.info("Mensaje recibido de: {} ({})", nombre, telefono);
        MensajeWhatsapp m = new MensajeWhatsapp(null, telefono, fromNumber,
                nombre, mensaje, "TEXTO", "ENTRANTE",
                "RECIBIDO", false, LocalDateTime.now());
        MensajeWhatsapp saved = repository.save(m);
        String mensajeKary = String.format("Mensaje de cliente\n%s (%s):\n%s", nombre, telefono, mensaje);
        enviarMensaje(telefonoKary, mensajeKary);
        log.info("Mensaje reenviado a Kary desde: {}", telefono);
        return toDto(saved);
    }

    @Override
    public String verificarWebhook(String mode, String token, String challenge) {
        log.info("Verificando webhook - mode: {}", mode);
        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            log.info("Webhook verificado exitosamente");
            return challenge;
        }
        log.warn("Fallo en verificacion de webhook");
        return "Error de verificacion";
    }

    @Override
    public String procesarWebhook(String body) {
        log.info("Webhook recibido, procesando mensaje");
        return "OK";
    }
}
