package com.reservatours.msreservas.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String clienteNombre;
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
