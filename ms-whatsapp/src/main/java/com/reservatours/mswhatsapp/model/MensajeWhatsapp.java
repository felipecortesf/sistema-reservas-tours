package com.reservatours.mswhatsapp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MensajeWhatsapp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String telefonoRemitente;
    String telefonoDestinatario;
    String nombreRemitente;
    @Column(columnDefinition = "TEXT")
    String contenido;
    String tipoMensaje;
    String direccion;
    String estado;
    Boolean procesado;
    LocalDateTime fechaMensaje;
}
