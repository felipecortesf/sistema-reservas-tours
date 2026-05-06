package com.reservatours.mscatalogotours.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String nombre;
    String descripcion;
    String destino;
    BigDecimal precio;
    Integer cuposDisponibles;
    LocalTime horaEmbarque;
    LocalDate fechaTour;
    Boolean activo;
}
