package com.belmonte.ms_pasajeros.exception;

public class EmailDuplicadoException extends RuntimeException {

    public EmailDuplicadoException(String email) {
        super("Ya existe un pasajero registrado con el email: " + email);
    }
}
