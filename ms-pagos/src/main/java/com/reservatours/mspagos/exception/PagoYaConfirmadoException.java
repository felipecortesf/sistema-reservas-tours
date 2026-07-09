package com.reservatours.mspagos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PagoYaConfirmadoException extends RuntimeException {
    public PagoYaConfirmadoException(String message) {
        super(message);
    }
}
