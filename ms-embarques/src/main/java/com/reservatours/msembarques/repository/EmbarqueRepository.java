package com.reservatours.msembarques.repository;

import com.reservatours.msembarques.model.Embarque;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface EmbarqueRepository extends JpaRepository<Embarque, Long> {
    List<Embarque> findByFechaEmbarque(LocalDate fecha);
    List<Embarque> findByEstado(String estado);
    List<Embarque> findByTourId(Long tourId);
    List<Embarque> findByNombreGuia(String nombreGuia);
}
