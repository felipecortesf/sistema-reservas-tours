package com.reservatours.mscomunicacionagencia.service;

import com.reservatours.mscomunicacionagencia.dto.MensajeDto;
import java.util.List;

public interface MensajeService {
    List<MensajeDto> findAll();
    MensajeDto findById(Long id);
    List<MensajeDto> findByReservaId(Long reservaId);
    MensajeDto clientePreguntaKary(Long reservaId, String telefono, String mensaje);
    MensajeDto karyRespondeCliente(Long reservaId, String mensaje);
    MensajeDto karyAlertaFelipe(Long reservaId, String mensaje);
    MensajeDto felipeLlamaCliente(Long reservaId, String mensaje);
}
