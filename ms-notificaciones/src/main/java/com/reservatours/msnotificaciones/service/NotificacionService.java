package com.reservatours.msnotificaciones.service;

import com.reservatours.msnotificaciones.dto.NotificacionDto;
import java.util.List;

public interface NotificacionService {
    List<NotificacionDto> findAll();
    NotificacionDto findById(Long id);
    List<NotificacionDto> findByTelefono(String telefono);
    List<NotificacionDto> findByReservaId(Long reservaId);
    NotificacionDto enviarNotificacion(NotificacionDto dto);
    Boolean deleteById(Long id);
}
