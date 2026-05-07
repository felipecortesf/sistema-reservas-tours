package com.reservatours.msnotificacionespush.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class NotificacionPush {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String titulo;
    @Column(columnDefinition = "TEXT")
    String mensaje;
    Long destinatarioId;
    String destinatarioTelefono;
    String tipo;
    String estado;
    LocalDateTime fechaEnvio;
    Boolean leida;
}
