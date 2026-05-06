package com.reservatours.msusuarios.service;

import com.reservatours.msusuarios.dto.UsuarioDto;
import java.util.List;

public interface UsuarioService {
    List<UsuarioDto> findAll();
    UsuarioDto findById(Long id);
    UsuarioDto findByEmail(String email);
    UsuarioDto save(UsuarioDto dto);
    Boolean deleteById(Long id);
}
