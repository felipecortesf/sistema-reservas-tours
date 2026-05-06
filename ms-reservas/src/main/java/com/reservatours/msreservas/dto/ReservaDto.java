package com.reservatours.msreservas.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReservaDto {
    Long id;
    @NotEmpty
    String clienteNombre;
    @NotEmpty
    String clienteTelefono;
    String clienteEmail;
    Long tourId;
    String tourNombre;
    LocalDate fechaTour;
    LocalTime horaEmbarque;
    String puntoEncuentro;
    String nombreGuia;
    String estado;
    Boolean notificacionEnviada;
    LocalDateTime fechaCreacion;
}
