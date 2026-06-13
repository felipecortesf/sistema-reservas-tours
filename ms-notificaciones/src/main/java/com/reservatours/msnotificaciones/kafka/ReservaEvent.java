package com.reservatours.msnotificaciones.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReservaEvent {
    Long id;
    String clienteNombre;
    String clienteTelefono;
    String clienteEmail;
    String tourNombre;
    String puntoEncuentro;
    String nombreGuia;
}
