package com.reservatours.mscomunicacionagencia.repository;

import com.reservatours.mscomunicacionagencia.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByReservaId(Long reservaId);
    List<Mensaje> findByOrigen(String origen);
    List<Mensaje> findByTipo(String tipo);
    List<Mensaje> findByTelefonoOrigen(String telefono);
}
