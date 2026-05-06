package com.reservatours.msusuarios.service.impl;

import com.reservatours.msusuarios.dto.UsuarioDto;
import com.reservatours.msusuarios.model.Usuario;
import com.reservatours.msusuarios.repository.UsuarioRepository;
import com.reservatours.msusuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

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
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public UsuarioDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public UsuarioDto findByEmail(String email) {
        return repository.findByEmail(email).map(this::toDto).orElse(null);
    }

    @Override
    public UsuarioDto save(UsuarioDto dto) {
        return toDto(repository.save(toEntity(dto)));
    }

    @Override
    public Boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
