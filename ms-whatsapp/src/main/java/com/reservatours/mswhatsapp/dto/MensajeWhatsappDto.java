package com.reservatours.mswhatsapp.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MensajeWhatsappDto {
    Long id;
    @NotEmpty
    String telefonoRemitente;
    String telefonoDestinatario;
    String nombreRemitente;
    @NotEmpty
    String contenido;
    String tipoMensaje;
    String direccion;
    String estado;
    Boolean procesado;
    LocalDateTime fechaMensaje;
}
