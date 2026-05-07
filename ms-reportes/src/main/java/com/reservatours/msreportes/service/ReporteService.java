package com.reservatours.msreportes.service;

import com.reservatours.msreportes.dto.ReporteDto;
import java.util.List;

public interface ReporteService {
    List<ReporteDto> findAll();
    ReporteDto findById(Long id);
    ReporteDto findByFecha(String fecha);
    ReporteDto generarReporteDiario();
    void enviarReporteDiarioWhatsApp();
}
