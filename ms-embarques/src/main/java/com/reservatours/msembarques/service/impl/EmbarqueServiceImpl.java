package com.reservatours.msembarques.service.impl;

import com.reservatours.msembarques.dto.EmbarqueDto;
import com.reservatours.msembarques.model.Embarque;
import com.reservatours.msembarques.repository.EmbarqueRepository;
import com.reservatours.msembarques.service.EmbarqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmbarqueServiceImpl implements EmbarqueService {

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
    public List<EmbarqueDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public EmbarqueDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public List<EmbarqueDto> findByFecha(String fecha) {
        return repository.findByFechaEmbarque(LocalDate.parse(fecha))
                .stream().map(this::toDto).toList();
    }

    @Override
    public List<EmbarqueDto> findByEstado(String estado) {
        return repository.findByEstado(estado)
                .stream().map(this::toDto).toList();
    }

    @Override
    public EmbarqueDto save(EmbarqueDto dto) {
        if (dto.getEstado() == null) dto.setEstado("PROGRAMADO");
        return toDto(repository.save(toEntity(dto)));
    }

    @Override
    public EmbarqueDto actualizarEstado(Long id, String estado, String observaciones) {
        return repository.findById(id).map(embarque -> {
            embarque.setEstado(estado);
            embarque.setObservaciones(observaciones);
            return toDto(repository.save(embarque));
        }).orElse(null);
    }

    @Override
    public EmbarqueDto reportarRetraso(Long id, String horaReal, String observaciones) {
        return repository.findById(id).map(embarque -> {
            embarque.setHoraEmbarqueReal(LocalTime.parse(horaReal));
            embarque.setEstado("RETRASADO");
            embarque.setObservaciones(observaciones);
            return toDto(repository.save(embarque));
        }).orElse(null);
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
