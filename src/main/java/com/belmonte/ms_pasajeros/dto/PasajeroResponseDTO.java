package com.belmonte.ms_pasajeros.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasajeroResponseDTO {

    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String empresa;
    private boolean activo;
}
