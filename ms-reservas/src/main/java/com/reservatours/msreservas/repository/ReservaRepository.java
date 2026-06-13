package com.reservatours.msreservas.repository;

import com.reservatours.msreservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByFechaTour(LocalDate fechaTour);
    List<Reserva> findByClienteTelefono(String telefono);
    List<Reserva> findByEstado(String estado);
    List<Reserva> findByFechaTourAndNotificacionEnviada(LocalDate fechaTour, Boolean notificacionEnviada);

    // Custom Query JPQL - cuenta reservas confirmadas para un tour especifico
    @Query("SELECT COUNT(r) FROM Reserva r WHERE r.tourId = :tourId AND r.estado = 'CONFIRMADA'")
    Long contarConfirmadasPorTour(@Param("tourId") Long tourId);

    // Custom Query SQL nativo - reservas de los proximos N dias
    @Query(value = "SELECT * FROM reserva WHERE fecha_tour BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL :dias DAY)", nativeQuery = true)
    List<Reserva> findReservasProximas(@Param("dias") int dias);
}
