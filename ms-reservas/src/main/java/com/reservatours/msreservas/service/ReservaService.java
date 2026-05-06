package com.reservatours.msreservas.service;

import com.reservatours.msreservas.dto.ReservaDto;
import java.util.List;

public interface ReservaService {
    List<ReservaDto> findAll();
    ReservaDto findById(Long id);
    List<ReservaDto> findByFecha(String fecha);
    List<ReservaDto> findByTelefono(String telefono);
    ReservaDto save(ReservaDto dto);
    Boolean deleteById(Long id);
    void enviarNotificacionesDiaSiguiente();
}
