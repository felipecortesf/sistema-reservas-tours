package com.reservatours.mswhatsapp.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleValidationErrors_retorna400ConCamposInvalidos() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("mensajeWhatsappDto", "contenido", "no debe estar vacio");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, String>> response = handler.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("no debe estar vacio", response.getBody().get("contenido"));
    }

    @Test
    void handleRuntimeException_retorna500() {
        RuntimeException ex = new RuntimeException("Error inesperado al enviar mensaje");

        ResponseEntity<Map<String, String>> response = handler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error inesperado al enviar mensaje", response.getBody().get("error"));
    }

    @Test
    void handleGenericException_retorna500ConDetalle() {
        Exception ex = new Exception("fallo generico no controlado");

        ResponseEntity<Map<String, String>> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("fallo generico no controlado", response.getBody().get("detalle"));
    }
}
