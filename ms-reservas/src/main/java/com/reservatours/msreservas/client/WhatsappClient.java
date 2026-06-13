package com.reservatours.msreservas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

@FeignClient(name = "ms-whatsapp")
public interface WhatsappClient {

    @PostMapping("/api/v1/whatsapp/enviar")
    void enviarMensaje(@RequestBody Map<String, Object> payload);
}
