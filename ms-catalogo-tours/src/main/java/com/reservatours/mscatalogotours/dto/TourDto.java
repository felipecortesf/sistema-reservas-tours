package com.reservatours.mscatalogotours.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TourDto {
    Long id;
    @NotEmpty
    String nombre;
    String descripcion;
    @NotEmpty
    String destino;
    @NotNull
    BigDecimal precio;
    @NotNull
    Integer cuposDisponibles;
    LocalTime horaEmbarque;
    LocalDate fechaTour;
    Boolean activo;
}
