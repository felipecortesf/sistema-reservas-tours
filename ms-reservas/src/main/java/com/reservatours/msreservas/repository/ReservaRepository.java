package com.reservatours.msreservas.repository;

import com.reservatours.msreservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByFechaTour(LocalDate fechaTour);
    List<Reserva> findByClienteTelefono(String telefono);
    List<Reserva> findByEstado(String estado);
    List<Reserva> findByFechaTourAndNotificacionEnviada(LocalDate fechaTour, Boolean notificacionEnviada);
}
