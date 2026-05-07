package com.reservatours.msembarques.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EmbarqueDto {
    Long id;
    Long tourId;
    @NotEmpty
    String tourNombre;
    LocalDate fechaEmbarque;
    LocalTime horaEmbarqueProgramada;
    LocalTime horaEmbarqueReal;
    @NotEmpty
    String puntoEncuentro;
    @NotEmpty
    String nombreGuia;
    String telefonoGuia;
    String estado;
    String observaciones;
}
