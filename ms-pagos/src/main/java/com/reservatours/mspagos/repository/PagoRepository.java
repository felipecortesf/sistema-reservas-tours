package com.reservatours.mspagos.repository;

import com.reservatours.mspagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByReservaId(Long reservaId);
    List<Pago> findByEstado(String estado);
    List<Pago> findByClienteTelefono(String telefono);
    List<Pago> findByMetodoPago(String metodoPago);

    // Custom Query JPQL - suma total recaudada por estado
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.estado = :estado")
    BigDecimal sumarMontoPorEstado(@Param("estado") String estado);

    // Custom Query SQL nativo - clientes con mas monto pagado (PAGADO)
    @Query(value = "SELECT cliente_nombre, SUM(monto) as total FROM pago WHERE estado = 'PAGADO' GROUP BY cliente_nombre ORDER BY total DESC LIMIT :limite", nativeQuery = true)
    List<Map<String, Object>> topClientesPorMontoPagado(@Param("limite") int limite);
}
