package com.reservatours.mscatalogotours.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CuposAgotadosException extends RuntimeException {
    public CuposAgotadosException(String message) {
        super(message);
    }
}
