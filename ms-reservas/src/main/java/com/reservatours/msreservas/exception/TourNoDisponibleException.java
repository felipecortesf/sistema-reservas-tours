package com.reservatours.msreservas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TourNoDisponibleException extends RuntimeException {
    public TourNoDisponibleException(String message) {
        super(message);
    }
}
