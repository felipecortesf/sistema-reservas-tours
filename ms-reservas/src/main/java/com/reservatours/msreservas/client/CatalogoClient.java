package com.reservatours.msreservas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.Map;

@FeignClient(name = "ms-catalogo-tours")
public interface CatalogoClient {

    @GetMapping("/api/v1/tours/{id}")
    Map<String, Object> getTourById(@PathVariable Long id);

    @PutMapping("/api/v1/tours/{id}/reducir-cupo")
    Map<String, Object> reducirCupo(@PathVariable Long id);
}
