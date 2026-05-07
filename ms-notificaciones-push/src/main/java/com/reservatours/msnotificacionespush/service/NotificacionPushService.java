package com.reservatours.msnotificacionespush.service;

import com.reservatours.msnotificacionespush.dto.NotificacionPushDto;
import java.util.List;

public interface NotificacionPushService {
    List<NotificacionPushDto> findAll();
    NotificacionPushDto findById(Long id);
    List<NotificacionPushDto> findByTelefono(String telefono);
    List<NotificacionPushDto> findNoLeidas(String telefono);
    NotificacionPushDto enviar(NotificacionPushDto dto);
    NotificacionPushDto marcarLeida(Long id);
    Boolean deleteById(Long id);
}
