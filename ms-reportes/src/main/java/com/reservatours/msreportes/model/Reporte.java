package com.reservatours.msreportes.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String tipo;
    LocalDate fechaReporte;
    Integer totalReservas;
    Integer totalEmbarques;
    Integer totalRetrasos;
    Integer totalClientesNoEncontrados;
    @Column(columnDefinition = "TEXT")
    String resumen;
    Boolean enviadoWhatsapp;
    LocalDateTime fechaCreacion;
}
