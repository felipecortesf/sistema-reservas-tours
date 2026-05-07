package com.reservatours.mswhatsapp.repository;

import com.reservatours.mswhatsapp.model.MensajeWhatsapp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensajeWhatsappRepository extends JpaRepository<MensajeWhatsapp, Long> {
    List<MensajeWhatsapp> findByTelefonoRemitente(String telefono);
    List<MensajeWhatsapp> findByDireccion(String direccion);
    List<MensajeWhatsapp> findByProcesado(Boolean procesado);
    List<MensajeWhatsapp> findByEstado(String estado);
}
