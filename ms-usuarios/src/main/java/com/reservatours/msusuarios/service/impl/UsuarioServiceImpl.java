package com.reservatours.msusuarios.service.impl;

import com.reservatours.msusuarios.dto.UsuarioDto;
import com.reservatours.msusuarios.model.Usuario;
import com.reservatours.msusuarios.repository.UsuarioRepository;
import com.reservatours.msusuarios.service.UsuarioService;
import com.reservatours.msusuarios.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);
    private final UsuarioRepository repository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    private UsuarioDto toDto(Usuario u) {
        return new UsuarioDto(u.getId(), u.getNombre(), u.getApellido(),
                u.getEmail(), u.getPassword(), u.getTelefono(), u.getRol());
    }

    private Usuario toEntity(UsuarioDto dto) {
        return new Usuario(dto.getId(), dto.getNombre(), dto.getApellido(),
                dto.getEmail(), dto.getPassword(), dto.getTelefono(), dto.getRol());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDto> findAll() {
        log.info("Consultando todos los usuarios");
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDto findById(Long id) {
        log.info("Buscando usuario con id: {}", id);
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDto findByEmail(String email) {
        log.info("Buscando usuario con email: {}", email);
        return repository.findByEmail(email)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
    }

    @Override
    @Transactional
    public UsuarioDto save(UsuarioDto dto) {
        log.info("Guardando usuario: {}", dto.getEmail());
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        try {
            return toDto(repository.save(toEntity(dto)));
        } catch (Exception e) {
            log.error("Error al guardar usuario: {}", e.getMessage());
            throw new RuntimeException("Error al guardar usuario: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Boolean deleteById(Long id) {
        log.info("Eliminando usuario con id: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        throw new ResourceNotFoundException("No se encontró usuario para eliminar con ID: " + id);
    }
}
