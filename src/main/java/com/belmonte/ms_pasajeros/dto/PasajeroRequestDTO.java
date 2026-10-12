package com.belmonte.ms_pasajeros.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasajeroRequestDTO {

    //id no se incluye porque la base de datos lo genera

    //activo no se incluye porque se maneja con PATCH /{id}/estado

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato valido")
    @Size(max = 150, message = "El email no puede superar los 150 caracteres")
    private String email;

    @Pattern(
            regexp = "^$|^\\+?[0-9]{8,15}$",
            message = "El telefono debe tener entre 8 y 15 digitos (puede iniciar con +)"
    )
    private String telefono;

    @Size(max = 150, message = "La empresa no puede superar los 150 caracteres")
    private String empresa;
}
