package com.reservatours.msreservas.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReservaDto {
    Long id;

    @NotEmpty(message = "El nombre del cliente es obligatorio")
    String clienteNombre;

    @NotEmpty(message = "El telefono del cliente es obligatorio")
    String clienteTelefono;

    String clienteEmail;

    @NotNull(message = "El tour es obligatorio")
    Long tourId;

    @NotEmpty(message = "El nombre del tour es obligatorio")
    String tourNombre;

    @NotNull(message = "La fecha del tour es obligatoria")
    LocalDate fechaTour;

    @NotNull(message = "La hora de embarque es obligatoria")
    LocalTime horaEmbarque;

    @NotEmpty(message = "El punto de encuentro es obligatorio")
    String puntoEncuentro;

    @NotEmpty(message = "El nombre del guia es obligatorio")
    String nombreGuia;

    String estado;
    Boolean notificacionEnviada;
    LocalDateTime fechaCreacion;
}
