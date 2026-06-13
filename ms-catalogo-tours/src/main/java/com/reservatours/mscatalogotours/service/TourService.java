package com.reservatours.mscatalogotours.service;

import com.reservatours.mscatalogotours.dto.TourDto;
import java.util.List;

public interface TourService {
    List<TourDto> findAll();
    List<TourDto> findActivos();
    TourDto findById(Long id);
    TourDto save(TourDto dto);
    Boolean deleteById(Long id);
    TourDto reducirCupo(Long id);
    List<TourDto> findToursOrdenadosPorPrecio();
    java.math.BigDecimal precioPromedioPorDestino(String destino);
}
