package com.reservatours.msnotificacionespush.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class NotificacionPushDto {
    Long id;
    @NotEmpty
    String titulo;
    @NotEmpty
    String mensaje;
    Long destinatarioId;
    String destinatarioTelefono;
    String tipo;
    String estado;
    LocalDateTime fechaEnvio;
    Boolean leida;
}
