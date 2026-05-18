package com.reservatours.mspagos.repository;

import com.reservatours.mspagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByReservaId(Long reservaId);
    List<Pago> findByEstado(String estado);
    List<Pago> findByClienteTelefono(String telefono);
    List<Pago> findByMetodoPago(String metodoPago);
}
