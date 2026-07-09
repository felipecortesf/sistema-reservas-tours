package com.reservatours.msusuarios.exception;

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
    void handleResourceNotFound_retorna404ConMensaje() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Usuario no encontrado con ID: 99");

        ResponseEntity<Map<String, String>> response = handler.handleResourceNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Usuario no encontrado con ID: 99", response.getBody().get("error"));
    }

    @Test
    void handleRuntimeException_retorna500ConMensaje() {
        RuntimeException ex = new RuntimeException("Error inesperado al guardar usuario");

        ResponseEntity<Map<String, String>> response = handler.handleRuntimeException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error inesperado al guardar usuario", response.getBody().get("error"));
    }

    @Test
    void handleValidationErrors_retorna400ConCamposInvalidos() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("usuarioDto", "nombre", "no debe estar vacio");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, String>> response = handler.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("no debe estar vacio", response.getBody().get("nombre"));
    }
}
