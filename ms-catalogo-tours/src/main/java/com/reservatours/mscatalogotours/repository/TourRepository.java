package com.reservatours.mscatalogotours.repository;

import com.reservatours.mscatalogotours.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByActivoTrue();
    List<Tour> findByDestino(String destino);
    List<Tour> findByCuposDisponiblesGreaterThan(Integer cupos);
}
