package com.reservatours.mscomunicacionagencia.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Mensaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String origen;
    String destino;
    String telefonoOrigen;
    String telefonoDestino;
    @Column(columnDefinition = "TEXT")
    String contenido;
    String tipo;
    String estado;
    Long reservaId;
    LocalDateTime fechaMensaje;
}
