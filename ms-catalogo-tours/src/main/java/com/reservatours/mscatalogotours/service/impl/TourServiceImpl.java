package com.reservatours.mscatalogotours.service.impl;

import com.reservatours.mscatalogotours.dto.TourDto;
import com.reservatours.mscatalogotours.model.Tour;
import com.reservatours.mscatalogotours.repository.TourRepository;
import com.reservatours.mscatalogotours.service.TourService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private final TourRepository repository;

    private TourDto toDto(Tour t) {
        return new TourDto(t.getId(), t.getNombre(), t.getDescripcion(),
                t.getDestino(), t.getPrecio(), t.getCuposDisponibles(),
                t.getHoraEmbarque(), t.getFechaTour(), t.getActivo());
    }

    private Tour toEntity(TourDto dto) {
        return new Tour(dto.getId(), dto.getNombre(), dto.getDescripcion(),
                dto.getDestino(), dto.getPrecio(), dto.getCuposDisponibles(),
                dto.getHoraEmbarque(), dto.getFechaTour(), dto.getActivo());
    }

    @Override
    public List<TourDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public List<TourDto> findActivos() {
        return repository.findByActivoTrue().stream().map(this::toDto).toList();
    }

    @Override
    public TourDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public TourDto save(TourDto dto) {
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

    @Override
    public TourDto reducirCupo(Long id) {
        return repository.findById(id).map(tour -> {
            if (tour.getCuposDisponibles() > 0) {
                tour.setCuposDisponibles(tour.getCuposDisponibles() - 1);
                return toDto(repository.save(tour));
            }
            return null;
        }).orElse(null);
    }
}
