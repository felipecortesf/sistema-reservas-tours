package com.reservatours.msnotificaciones.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NotificacionDto {
    Long id;
    @NotEmpty
    String destinatarioTelefono;
    @NotEmpty
    String destinatarioNombre;
    @NotEmpty
    String mensaje;
    String tipo;
    String estado;
    LocalDateTime fechaEnvio;
    Long reservaId;
}
