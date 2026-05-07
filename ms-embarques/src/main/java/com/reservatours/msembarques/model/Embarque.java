package com.reservatours.msembarques.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Embarque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    Long tourId;
    String tourNombre;
    LocalDate fechaEmbarque;
    LocalTime horaEmbarqueProgramada;
    LocalTime horaEmbarqueReal;
    String puntoEncuentro;
    String nombreGuia;
    String telefonoGuia;
    String estado;
    @Column(columnDefinition = "TEXT")
    String observaciones;
}
