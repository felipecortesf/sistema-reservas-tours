package com.reservatours.mswhatsapp.controller;

import com.reservatours.mswhatsapp.dto.MensajeWhatsappDto;
import com.reservatours.mswhatsapp.service.WhatsappService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/whatsapp")
@Tag(name = "WhatsApp", description = "Integracion con Twilio WhatsApp Business API")
public class WhatsappController {

    private final WhatsappService service;

    @Operation(summary = "Listar todos los mensajes WhatsApp")
    @GetMapping
    public ResponseEntity<List<MensajeWhatsappDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Listar mensajes no procesados")
    @GetMapping("/no-procesados")
    public ResponseEntity<List<MensajeWhatsappDto>> findNoProcessados() {
        return ResponseEntity.ok(service.findNoProcessados());
    }

    @Operation(summary = "Enviar mensaje WhatsApp via Twilio")
    @PostMapping("/enviar")
    public ResponseEntity<MensajeWhatsappDto> enviar(
            @RequestParam String telefono,
            @RequestParam String mensaje) {
        return ResponseEntity.ok(service.enviarMensaje(telefono, mensaje));
    }

    @Operation(summary = "Registrar mensaje entrante manualmente")
    @PostMapping("/recibir")
    public ResponseEntity<MensajeWhatsappDto> recibir(
            @RequestParam String telefono,
            @RequestParam String nombre,
            @RequestParam String mensaje) {
        return ResponseEntity.ok(service.recibirMensaje(telefono, nombre, mensaje));
    }

    @Operation(summary = "Verificar webhook de Twilio (GET)")
    @GetMapping("/webhook")
    public ResponseEntity<String> verificarWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {
        return ResponseEntity.ok(service.verificarWebhook(mode, token, challenge));
    }

    @Operation(summary = "Recibir mensajes entrantes de Twilio (POST webhook)")
    @PostMapping(value = "/webhook", consumes = {"application/x-www-form-urlencoded", "application/json", "*/*"})
    public ResponseEntity<String> recibirWebhook(@RequestBody String body) {
        return ResponseEntity.ok(service.procesarWebhook(body));
    }
}
