package com.reservatours.msusuarios.dto;

import com.reservatours.msusuarios.model.Rol;
import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UsuarioDto {
    Long id;
    @NotEmpty
    String nombre;
    @NotEmpty
    String apellido;
    @Email
    String email;
    @JsonIgnore
    String password;
    String telefono;
    Rol rol;
}
