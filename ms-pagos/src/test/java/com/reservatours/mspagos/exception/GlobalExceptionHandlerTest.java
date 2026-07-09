package com.reservatours.mspagos.exception;

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
    void handleResourceNotFound_retorna404() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Pago no encontrado con ID: 99");

        ResponseEntity<Map<String, String>> response = handler.handleResourceNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Pago no encontrado con ID: 99", response.getBody().get("error"));
    }

    @Test
    void handlePagoYaConfirmado_retorna409() {
        PagoYaConfirmadoException ex = new PagoYaConfirmadoException("El pago con ID: 1 ya se encuentra confirmado");

        ResponseEntity<Map<String, String>> response = handler.handlePagoYaConfirmado(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody().get("error").contains("confirmado"));
    }

    @Test
    void handleRuntimeException_retorna500() {
        RuntimeException ex = new RuntimeException("Error al guardar pago");

        ResponseEntity<Map<String, String>> response = handler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void handleValidationErrors_retorna400ConCamposInvalidos() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("pagoDto", "monto", "El monto debe ser mayor a 0");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, String>> response = handler.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El monto debe ser mayor a 0", response.getBody().get("monto"));
    }
}
