package com.reservatours.msusuarios.service.impl;

import com.reservatours.msusuarios.dto.UsuarioDto;
import com.reservatours.msusuarios.exception.ResourceNotFoundException;
import com.reservatours.msusuarios.model.Rol;
import com.reservatours.msusuarios.model.Usuario;
import com.reservatours.msusuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl service;

    private Usuario usuario;
    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol(1L, "ADMIN");
        usuario = new Usuario(1L, "Felipe", "Cortes", "felipe@email.com", "hashedPass", "56912345678", rol);
    }

    @Test
    void findById_existente_retornaUsuarioDto() {
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioDto resultado = service.findById(1L);

        assertNotNull(resultado);
        assertEquals("Felipe", resultado.getNombre());
    }

    @Test
    void findById_inexistente_lanzaResourceNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));
    }

    @Test
    void findByEmail_existente_retornaUsuarioDto() {
        when(repository.findByEmail("felipe@email.com")).thenReturn(Optional.of(usuario));

        UsuarioDto resultado = service.findByEmail("felipe@email.com");

        assertEquals("felipe@email.com", resultado.getEmail());
    }

    @Test
    void save_encriptaPasswordConBCryptAntesDeGuardar() {
        UsuarioDto nuevoUsuario = new UsuarioDto(null, "Maria", "Lopez", "maria@email.com", "plainPassword", "56987654321", rol);

        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword123");
        when(repository.save(any(Usuario.class))).thenReturn(usuario);

        service.save(nuevoUsuario);

        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(repository, times(1)).save(any(Usuario.class));
    }

    @Test
    void findAll_retornaListaDeUsuarios() {
        when(repository.findAll()).thenReturn(List.of(usuario));

        List<UsuarioDto> resultado = service.findAll();

        assertEquals(1, resultado.size());
        assertEquals("ADMIN", resultado.get(0).getRol().getNombre());
    }

    @Test
    void deleteById_existente_retornaTrue() {
        when(repository.existsById(1L)).thenReturn(true);

        Boolean resultado = service.deleteById(1L);

        assertTrue(resultado);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_inexistente_lanzaResourceNotFoundException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteById(99L));
    }
}
