package com.reservatours.msembarques.service.impl;

import com.reservatours.msembarques.dto.EmbarqueDto;
import com.reservatours.msembarques.model.Embarque;
import com.reservatours.msembarques.repository.EmbarqueRepository;
import com.reservatours.msembarques.service.EmbarqueService;
import com.reservatours.msembarques.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbarqueServiceImpl implements EmbarqueService {

    private static final Logger log = LoggerFactory.getLogger(EmbarqueServiceImpl.class);
    private final EmbarqueRepository repository;

    private EmbarqueDto toDto(Embarque e) {
        return new EmbarqueDto(e.getId(), e.getTourId(), e.getTourNombre(),
                e.getFechaEmbarque(), e.getHoraEmbarqueProgramada(),
                e.getHoraEmbarqueReal(), e.getPuntoEncuentro(),
                e.getNombreGuia(), e.getTelefonoGuia(),
                e.getEstado(), e.getObservaciones());
    }

    private Embarque toEntity(EmbarqueDto dto) {
        return new Embarque(dto.getId(), dto.getTourId(), dto.getTourNombre(),
                dto.getFechaEmbarque(), dto.getHoraEmbarqueProgramada(),
                dto.getHoraEmbarqueReal(), dto.getPuntoEncuentro(),
                dto.getNombreGuia(), dto.getTelefonoGuia(),
                dto.getEstado(), dto.getObservaciones());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmbarqueDto> findAll() {
        log.info("Consultando todos los embarques");
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EmbarqueDto findById(Long id) {
        log.info("Buscando embarque con id: {}", id);
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Embarque no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmbarqueDto> findByFecha(String fecha) {
        log.info("Buscando embarques para fecha: {}", fecha);
        return repository.findByFechaEmbarque(LocalDate.parse(fecha)).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmbarqueDto> findByEstado(String estado) {
        log.info("Buscando embarques con estado: {}", estado);
        return repository.findByEstado(estado).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional
    public EmbarqueDto save(EmbarqueDto dto) {
        log.info("Guardando embarque para tour: {}", dto.getTourNombre());
        if (dto.getEstado() == null) dto.setEstado("PROGRAMADO");
        try {
            return toDto(repository.save(toEntity(dto)));
        } catch (Exception e) {
            log.error("Error al guardar embarque: {}", e.getMessage());
            throw new RuntimeException("Error al guardar embarque: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public EmbarqueDto actualizarEstado(Long id, String estado, String observaciones) {
        log.info("Actualizando estado embarque id: {} a: {}", id, estado);
        return repository.findById(id).map(embarque -> {
            embarque.setEstado(estado);
            embarque.setObservaciones(observaciones);
            return toDto(repository.save(embarque));
        }).orElseThrow(() -> new ResourceNotFoundException("No se pudo actualizar, embarque no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public EmbarqueDto reportarRetraso(Long id, String horaReal, String observaciones) {
        log.warn("Reportando retraso en embarque id: {}, hora real: {}", id, horaReal);
        return repository.findById(id).map(embarque -> {
            embarque.setHoraEmbarqueReal(LocalTime.parse(horaReal));
            embarque.setEstado("RETRASADO");
            embarque.setObservaciones(observaciones);
            return toDto(repository.save(embarque));
        }).orElseThrow(() -> new ResourceNotFoundException("No se pudo reportar retraso, embarque no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public Boolean deleteById(Long id) {
        log.info("Eliminando embarque con id: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        throw new ResourceNotFoundException("No se encontró embarque para eliminar con ID: " + id);
    }
}
