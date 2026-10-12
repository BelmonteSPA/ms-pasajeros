package com.belmonte.ms_pasajeros.exception;

public class PasajeroNoEncontradoException extends RuntimeException {

    public PasajeroNoEncontradoException(Long id) {
        super("El pasajero con ID: " + id + " no existe");
    }
}
