package com.reservatours.mscatalogotours.repository;

import com.reservatours.mscatalogotours.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByActivoTrue();
    List<Tour> findByDestino(String destino);
    List<Tour> findByCuposDisponiblesGreaterThan(Integer cupos);

    // Custom Query JPQL - tours activos ordenados por precio ascendente
    @Query("SELECT t FROM Tour t WHERE t.activo = true ORDER BY t.precio ASC")
    List<Tour> findToursActivosOrdenadosPorPrecio();

    // Custom Query SQL nativo - precio promedio de tours por destino
    @Query(value = "SELECT AVG(precio) FROM tour WHERE destino = :destino AND activo = true", nativeQuery = true)
    BigDecimal precioPromedioPorDestino(@Param("destino") String destino);
}
