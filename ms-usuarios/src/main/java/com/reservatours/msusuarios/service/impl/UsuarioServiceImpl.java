package com.reservatours.msusuarios.service.impl;

import com.reservatours.msusuarios.dto.UsuarioDto;
import com.reservatours.msusuarios.model.Usuario;
import com.reservatours.msusuarios.repository.UsuarioRepository;
import com.reservatours.msusuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);
    private final UsuarioRepository repository;

    private UsuarioDto toDto(Usuario u) {
        return new UsuarioDto(u.getId(), u.getNombre(), u.getApellido(),
                u.getEmail(), u.getPassword(), u.getTelefono(), u.getRol());
    }

    private Usuario toEntity(UsuarioDto dto) {
        return new Usuario(dto.getId(), dto.getNombre(), dto.getApellido(),
                dto.getEmail(), dto.getPassword(), dto.getTelefono(), dto.getRol());
    }

    @Override
    public List<UsuarioDto> findAll() {
        log.info("Consultando todos los usuarios");
        List<UsuarioDto> usuarios = repository.findAll().stream().map(this::toDto).toList();
        log.info("Total usuarios encontrados: {}", usuarios.size());
        return usuarios;
    }

    @Override
    public UsuarioDto findById(Long id) {
        log.info("Buscando usuario con id: {}", id);
        return repository.findById(id).map(u -> {
            log.info("Usuario encontrado: {}", u.getEmail());
            return toDto(u);
        }).orElseGet(() -> {
            log.warn("Usuario no encontrado con id: {}", id);
            return null;
        });
    }

    @Override
    public UsuarioDto findByEmail(String email) {
        log.info("Buscando usuario con email: {}", email);
        return repository.findByEmail(email).map(u -> {
            log.info("Usuario encontrado: {}", u.getEmail());
            return toDto(u);
        }).orElseGet(() -> {
            log.warn("Usuario no encontrado con email: {}", email);
            return null;
        });
    }

    @Override
    public UsuarioDto save(UsuarioDto dto) {
        log.info("Guardando usuario: {}", dto.getEmail());
        try {
            UsuarioDto saved = toDto(repository.save(toEntity(dto)));
            log.info("Usuario guardado exitosamente con id: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Error al guardar usuario: {}", e.getMessage());
            throw new RuntimeException("Error al guardar usuario: " + e.getMessage());
        }
    }

    @Override
    public Boolean deleteById(Long id) {
        log.info("Eliminando usuario con id: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Usuario eliminado exitosamente con id: {}", id);
            return true;
        }
        log.warn("No se encontro usuario para eliminar con id: {}", id);
        return false;
    }
}
