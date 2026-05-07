package com.reservatours.mscomunicacionagencia.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "ms-reservas")
public interface ReservaClient {

    @GetMapping("/api/v1/reservas/{id}")
    Map<String, Object> getReservaById(@PathVariable Long id);

    @GetMapping("/api/v1/reservas/fecha/{fecha}")
    java.util.List<Map<String, Object>> getReservasByFecha(@PathVariable String fecha);
}
