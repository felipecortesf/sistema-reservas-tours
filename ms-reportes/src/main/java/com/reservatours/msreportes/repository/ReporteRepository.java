package com.reservatours.msreportes.repository;

import com.reservatours.msreportes.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByTipo(String tipo);
    Optional<Reporte> findByFechaReporte(LocalDate fecha);
    List<Reporte> findByEnviadoWhatsapp(Boolean enviado);
}
