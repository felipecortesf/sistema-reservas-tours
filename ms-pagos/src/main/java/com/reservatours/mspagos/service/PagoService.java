package com.reservatours.mspagos.service;

import com.reservatours.mspagos.dto.PagoDto;
import java.util.List;

public interface PagoService {
    List<PagoDto> findAll();
    PagoDto findById(Long id);
    List<PagoDto> findByReservaId(Long reservaId);
    List<PagoDto> findByEstado(String estado);
    PagoDto save(PagoDto dto);
    PagoDto confirmarPago(Long id);
    Boolean deleteById(Long id);
    java.math.BigDecimal sumarMontoPorEstado(String estado);
    List<java.util.Map<String, Object>> topClientesPorMontoPagado(int limite);
}
