package com.reservatours.mspagos.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PagoDto {
    Long id;

    @NotNull(message = "El id de la reserva es obligatorio")
    Long reservaId;

    @NotEmpty(message = "El nombre del cliente es obligatorio")
    String clienteNombre;

    @NotEmpty(message = "El telefono del cliente es obligatorio")
    String clienteTelefono;

    @NotEmpty(message = "El nombre del tour es obligatorio")
    String tourNombre;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    BigDecimal monto;

    @NotEmpty(message = "El metodo de pago es obligatorio")
    String metodoPago;

    String estado;
    LocalDateTime fechaPago;
    String observaciones;
}
