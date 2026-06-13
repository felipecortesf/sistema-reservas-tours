package com.reservatours.msnotificaciones.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagoEvent {
    Long id;
    Long reservaId;
    String clienteNombre;
    String clienteTelefono;
    String tourNombre;
    BigDecimal monto;
    String metodoPago;
    String estado;
}
