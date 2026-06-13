package com.reservatours.msusuarios.controller;

import com.reservatours.msusuarios.config.JwtUtil;
import com.reservatours.msusuarios.model.Usuario;
import com.reservatours.msusuarios.repository.UsuarioRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Data
    static class LoginRequest {
        String email;
        String password;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        log.info("Intento de login: {}", request.getEmail());

        return repository.findByEmail(request.getEmail())
            .filter(u -> passwordEncoder.matches(request.getPassword(), u.getPassword()))
            .map(u -> {
                String token = jwtUtil.generateToken(u.getEmail(), u.getRol().getNombre());
                log.info("Login exitoso para: {} con rol {}", u.getEmail(), u.getRol().getNombre());
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("email", u.getEmail());
                response.put("rol", u.getRol().getNombre());
                return ResponseEntity.ok(response);
            })
            .orElseGet(() -> {
                log.warn("Login fallido para: {}", request.getEmail());
                Map<String, String> error = new HashMap<>();
                error.put("error", "Credenciales invalidas");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
            });
    }
}
