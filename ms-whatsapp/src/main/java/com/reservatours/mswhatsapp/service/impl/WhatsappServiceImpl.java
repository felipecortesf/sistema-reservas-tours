package com.reservatours.mswhatsapp.service.impl;

import com.reservatours.mswhatsapp.dto.MensajeWhatsappDto;
import com.reservatours.mswhatsapp.model.MensajeWhatsapp;
import com.reservatours.mswhatsapp.repository.MensajeWhatsappRepository;
import com.reservatours.mswhatsapp.service.WhatsappService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WhatsappServiceImpl implements WhatsappService {

    private final MensajeWhatsappRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${whatsapp.token}")
    private String whatsappToken;

    @Value("${whatsapp.phone.number.id}")
    private String phoneNumberId;

    @Value("${whatsapp.verify.token}")
    private String verifyToken;

    @Value("${telefono.kary}")
    private String telefonoKary;

    private MensajeWhatsappDto toDto(MensajeWhatsapp m) {
        return new MensajeWhatsappDto(m.getId(), m.getTelefonoRemitente(),
                m.getTelefonoDestinatario(), m.getNombreRemitente(),
                m.getContenido(), m.getTipoMensaje(), m.getDireccion(),
                m.getEstado(), m.getProcesado(), m.getFechaMensaje());
    }

    @Override
    public List<MensajeWhatsappDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public MensajeWhatsappDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public List<MensajeWhatsappDto> findNoProcessados() {
        return repository.findByProcesado(false).stream().map(this::toDto).toList();
    }

    @Override
    public MensajeWhatsappDto enviarMensaje(String telefono, String mensaje) {
        String url = "https://graph.facebook.com/v25.0/" + phoneNumberId + "/messages";
        String body = String.format(
            "{\"messaging_product\":\"whatsapp\",\"to\":\"%s\",\"type\":\"text\",\"text\":{\"body\":\"%s\"}}",
            telefono, mensaje.replace("\"", "'").replace("\n", "\\n")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(whatsappToken);

        String estado = "ENVIADO";
        try {
            restTemplate.postForEntity(url, new HttpEntity<>(body, headers), String.class);
        } catch (Exception e) {
            estado = "ERROR";
            System.out.println("Error WhatsApp: " + e.getMessage());
        }

        MensajeWhatsapp m = new MensajeWhatsapp(null, phoneNumberId, telefono,
                "Sistema", mensaje, "TEXTO", "SALIENTE",
                estado, true, LocalDateTime.now());
        return toDto(repository.save(m));
    }

    @Override
    public MensajeWhatsappDto recibirMensaje(String telefono, String nombre, String mensaje) {
        // Guardar mensaje recibido
        MensajeWhatsapp m = new MensajeWhatsapp(null, telefono, phoneNumberId,
                nombre, mensaje, "TEXTO", "ENTRANTE",
                "RECIBIDO", false, LocalDateTime.now());
        MensajeWhatsapp saved = repository.save(m);

        // Reenviar a Kary automaticamente
        String mensajeKary = String.format(
            "📱 Mensaje de cliente\\n👤 %s (%s):\\n%s",
            nombre, telefono, mensaje
        );
        enviarMensaje(telefonoKary, mensajeKary);

        return toDto(saved);
    }

    @Override
    public String verificarWebhook(String mode, String token, String challenge) {
        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            return challenge;
        }
        return "Error de verificacion";
    }

    @Override
    public String procesarWebhook(String body) {
        System.out.println("Webhook recibido: " + body);
        return "OK";
    }
}
