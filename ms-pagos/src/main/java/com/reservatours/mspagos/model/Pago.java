package com.reservatours.mspagos.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long reservaId;
    String clienteNombre;
    String clienteTelefono;
    String tourNombre;
    BigDecimal monto;
    String metodoPago;
    String estado;
    LocalDateTime fechaPago;
    @Column(columnDefinition = "TEXT")
    String observaciones;
}
