package com.reservatours.mswhatsapp.service;

import com.reservatours.mswhatsapp.dto.MensajeWhatsappDto;
import java.util.List;

public interface WhatsappService {
    List<MensajeWhatsappDto> findAll();
    MensajeWhatsappDto findById(Long id);
    List<MensajeWhatsappDto> findNoProcessados();
    MensajeWhatsappDto enviarMensaje(String telefono, String mensaje);
    MensajeWhatsappDto recibirMensaje(String telefono, String nombre, String mensaje);
    String procesarWebhook(String body);
    String verificarWebhook(String mode, String token, String challenge);
}
