package com.reservatours.msusuarios.dto;

import com.reservatours.msusuarios.model.Rol;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioDto {
    Long id;
    @NotEmpty
    String nombre;
    @NotEmpty
    String apellido;
    @Email
    String email;
    String password;
    String telefono;
    Rol rol;
}
