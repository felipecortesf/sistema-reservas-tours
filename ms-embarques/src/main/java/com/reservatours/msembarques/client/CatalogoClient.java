package com.reservatours.msembarques.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@FeignClient(name = "ms-catalogo-tours")
public interface CatalogoClient {

    @GetMapping("/api/v1/tours/{id}")
    Map<String, Object> getTourById(@PathVariable Long id);

    @GetMapping("/api/v1/tours/activos")
    java.util.List<Map<String, Object>> getToursActivos();
}
