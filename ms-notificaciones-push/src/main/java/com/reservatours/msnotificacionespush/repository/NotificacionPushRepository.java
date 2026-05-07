package com.reservatours.msnotificacionespush.repository;

import com.reservatours.msnotificacionespush.model.NotificacionPush;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacionPushRepository extends JpaRepository<NotificacionPush, Long> {
    List<NotificacionPush> findByDestinatarioTelefono(String telefono);
    List<NotificacionPush> findByDestinatarioId(Long id);
    List<NotificacionPush> findByLeida(Boolean leida);
    List<NotificacionPush> findByTipo(String tipo);
    List<NotificacionPush> findByEstado(String estado);
}
