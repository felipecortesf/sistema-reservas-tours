package com.reservatours.mscatalogotours.service.impl;

import com.reservatours.mscatalogotours.dto.TourDto;
import com.reservatours.mscatalogotours.model.Tour;
import com.reservatours.mscatalogotours.repository.TourRepository;
import com.reservatours.mscatalogotours.service.TourService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {

    private static final Logger log = LoggerFactory.getLogger(TourServiceImpl.class);
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
        log.info("Consultando todos los tours");
        List<TourDto> tours = repository.findAll().stream().map(this::toDto).toList();
        log.info("Total tours encontrados: {}", tours.size());
        return tours;
    }

    @Override
    public List<TourDto> findActivos() {
        log.info("Consultando tours activos");
        List<TourDto> tours = repository.findByActivoTrue().stream().map(this::toDto).toList();
        log.info("Total tours activos: {}", tours.size());
        return tours;
    }

    @Override
    public TourDto findById(Long id) {
        log.info("Buscando tour con id: {}", id);
        return repository.findById(id).map(t -> {
            log.info("Tour encontrado: {}", t.getNombre());
            return toDto(t);
        }).orElseGet(() -> {
            log.warn("Tour no encontrado con id: {}", id);
            return null;
        });
    }

    @Override
    public TourDto save(TourDto dto) {
        log.info("Guardando tour: {}", dto.getNombre());
        try {
            TourDto saved = toDto(repository.save(toEntity(dto)));
            log.info("Tour guardado exitosamente con id: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Error al guardar tour: {}", e.getMessage());
            throw new RuntimeException("Error al guardar tour: " + e.getMessage());
        }
    }

    @Override
    public Boolean deleteById(Long id) {
        log.info("Eliminando tour con id: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Tour eliminado exitosamente con id: {}", id);
            return true;
        }
        log.warn("No se encontro tour para eliminar con id: {}", id);
        return false;
    }

    @Override
    public TourDto reducirCupo(Long id) {
        log.info("Reduciendo cupo del tour con id: {}", id);
        return repository.findById(id).map(tour -> {
            if (tour.getCuposDisponibles() > 0) {
                tour.setCuposDisponibles(tour.getCuposDisponibles() - 1);
                log.info("Cupo reducido. Cupos restantes: {}", tour.getCuposDisponibles());
                return toDto(repository.save(tour));
            }
            log.warn("No hay cupos disponibles para tour id: {}", id);
            return null;
        }).orElseGet(() -> {
            log.warn("Tour no encontrado con id: {}", id);
            return null;
        });
    }

    @Override
    public List<TourDto> findToursOrdenadosPorPrecio() {
        log.info("Consultando tours activos ordenados por precio");
        return repository.findToursActivosOrdenadosPorPrecio().stream().map(this::toDto).toList();
    }

    @Override
    public java.math.BigDecimal precioPromedioPorDestino(String destino) {
        log.info("Calculando precio promedio para destino: {}", destino);
        return repository.precioPromedioPorDestino(destino);
    }
}
