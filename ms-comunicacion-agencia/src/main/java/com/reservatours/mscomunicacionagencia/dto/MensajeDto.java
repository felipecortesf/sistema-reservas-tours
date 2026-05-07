package com.reservatours.mscomunicacionagencia.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MensajeDto {
    Long id;
    @NotEmpty
    String origen;
    @NotEmpty
    String destino;
    String telefonoOrigen;
    String telefonoDestino;
    @NotEmpty
    String contenido;
    String tipo;
    String estado;
    Long reservaId;
    LocalDateTime fechaMensaje;
}
