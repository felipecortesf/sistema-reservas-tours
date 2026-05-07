package com.reservatours.msreportes.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReporteDto {
    Long id;
    @NotEmpty
    String tipo;
    LocalDate fechaReporte;
    Integer totalReservas;
    Integer totalEmbarques;
    Integer totalRetrasos;
    Integer totalClientesNoEncontrados;
    String resumen;
    Boolean enviadoWhatsapp;
    LocalDateTime fechaCreacion;
}
