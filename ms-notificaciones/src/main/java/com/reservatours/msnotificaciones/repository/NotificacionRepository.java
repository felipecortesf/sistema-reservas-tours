package com.reservatours.msnotificaciones.repository;

import com.reservatours.msnotificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByEstado(String estado);
    List<Notificacion> findByDestinatarioTelefono(String telefono);
    List<Notificacion> findByReservaId(Long reservaId);
    List<Notificacion> findByTipo(String tipo);
}
