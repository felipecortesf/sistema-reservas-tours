package com.reservatours.msembarques.service;

import com.reservatours.msembarques.dto.EmbarqueDto;
import java.util.List;

public interface EmbarqueService {
    List<EmbarqueDto> findAll();
    EmbarqueDto findById(Long id);
    List<EmbarqueDto> findByFecha(String fecha);
    List<EmbarqueDto> findByEstado(String estado);
    EmbarqueDto save(EmbarqueDto dto);
    EmbarqueDto actualizarEstado(Long id, String estado, String observaciones);
    EmbarqueDto reportarRetraso(Long id, String horaReal, String observaciones);
    Boolean deleteById(Long id);
}
