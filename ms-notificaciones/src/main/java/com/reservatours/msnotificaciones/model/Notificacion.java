package com.reservatours.msnotificaciones.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String destinatarioTelefono;
    String destinatarioNombre;
    @Column(columnDefinition = "TEXT")
    String mensaje;
    String tipo;
    String estado;
    LocalDateTime fechaEnvio;
    Long reservaId;
}
