package com.reservatours.mswhatsapp.controller;

import com.reservatours.mswhatsapp.dto.MensajeWhatsappDto;
import com.reservatours.mswhatsapp.service.WhatsappService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/whatsapp")
public class WhatsappController {

    private final WhatsappService service;

    @GetMapping
    public ResponseEntity<List<MensajeWhatsappDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/no-procesados")
    public ResponseEntity<List<MensajeWhatsappDto>> findNoProcessados() {
        return ResponseEntity.ok(service.findNoProcessados());
    }

    @PostMapping("/enviar")
    public ResponseEntity<MensajeWhatsappDto> enviar(
            @RequestParam String telefono,
            @RequestParam String mensaje) {
        return ResponseEntity.ok(service.enviarMensaje(telefono, mensaje));
    }

    @PostMapping("/recibir")
    public ResponseEntity<MensajeWhatsappDto> recibir(
            @RequestParam String telefono,
            @RequestParam String nombre,
            @RequestParam String mensaje) {
        return ResponseEntity.ok(service.recibirMensaje(telefono, nombre, mensaje));
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> verificarWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {
        return ResponseEntity.ok(service.verificarWebhook(mode, token, challenge));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> recibirWebhook(@RequestBody String body) {
        return ResponseEntity.ok(service.procesarWebhook(body));
    }
}
