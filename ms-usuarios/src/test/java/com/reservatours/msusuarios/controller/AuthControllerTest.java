package com.reservatours.msusuarios.controller;

import com.reservatours.msusuarios.config.JwtUtil;
import com.reservatours.msusuarios.model.Rol;
import com.reservatours.msusuarios.model.Usuario;
import com.reservatours.msusuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController controller;

    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol(1L, "ADMIN");
        usuario = new Usuario(1L, "Felipe", "Cortes", "felipe@email.com", "hashedPass", "56912345678", rol);
    }

    @Test
    void login_credencialesValidas_retorna200ConToken() {
        AuthController.LoginRequest request = new AuthController.LoginRequest();
        request.setEmail("felipe@email.com");
        request.setPassword("plainPassword");

        when(repository.findByEmail("felipe@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("plainPassword", "hashedPass")).thenReturn(true);
        when(jwtUtil.generateToken("felipe@email.com", "ADMIN")).thenReturn("token-jwt-simulado");

        ResponseEntity<Map<String, String>> response = controller.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token-jwt-simulado", response.getBody().get("token"));
        assertEquals("felipe@email.com", response.getBody().get("email"));
        assertEquals("ADMIN", response.getBody().get("rol"));
    }

    @Test
    void login_usuarioInexistente_retorna401() {
        AuthController.LoginRequest request = new AuthController.LoginRequest();
        request.setEmail("noexiste@email.com");
        request.setPassword("cualquierPassword");

        when(repository.findByEmail("noexiste@email.com")).thenReturn(Optional.empty());

        ResponseEntity<Map<String, String>> response = controller.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciales invalidas", response.getBody().get("error"));
    }

    @Test
    void login_passwordIncorrecta_retorna401() {
        AuthController.LoginRequest request = new AuthController.LoginRequest();
        request.setEmail("felipe@email.com");
        request.setPassword("passwordIncorrecta");

        when(repository.findByEmail("felipe@email.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        ResponseEntity<Map<String, String>> response = controller.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciales invalidas", response.getBody().get("error"));
    }
}
