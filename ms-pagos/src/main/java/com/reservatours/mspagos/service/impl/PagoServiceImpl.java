package com.reservatours.mspagos.service.impl;

import com.reservatours.mspagos.dto.PagoDto;
import com.reservatours.mspagos.model.Pago;
import com.reservatours.mspagos.repository.PagoRepository;
import com.reservatours.mspagos.service.PagoService;
import com.reservatours.mspagos.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoServiceImpl.class);
    private final PagoRepository repository;

    private PagoDto toDto(Pago p) {
        return new PagoDto(p.getId(), p.getReservaId(), p.getClienteNombre(),
                p.getClienteTelefono(), p.getTourNombre(), p.getMonto(),
                p.getMetodoPago(), p.getEstado(), p.getFechaPago(), p.getObservaciones());
    }

    private Pago toEntity(PagoDto dto) {
        return new Pago(dto.getId(), dto.getReservaId(), dto.getClienteNombre(),
                dto.getClienteTelefono(), dto.getTourNombre(), dto.getMonto(),
                dto.getMetodoPago(), dto.getEstado(), dto.getFechaPago(), dto.getObservaciones());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDto> findAll() {
        log.info("Consultando todos los pagos");
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PagoDto findById(Long id) {
        log.info("Buscando pago con id: {}", id);
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDto> findByReservaId(Long reservaId) {
        log.info("Buscando pagos para reserva: {}", reservaId);
        return repository.findByReservaId(reservaId).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoDto> findByEstado(String estado) {
        log.info("Buscando pagos con estado: {}", estado);
        return repository.findByEstado(estado).stream().map(this::toDto).toList();
    }

    @Override
    @Transactional
    public PagoDto save(PagoDto dto) {
        log.info("Guardando pago para cliente: {}", dto.getClienteNombre());
        try {
            if (dto.getEstado() == null) dto.setEstado("PENDIENTE");
            if (dto.getFechaPago() == null) dto.setFechaPago(LocalDateTime.now());
            return toDto(repository.save(toEntity(dto)));
        } catch (Exception e) {
            log.error("Error al guardar pago: {}", e.getMessage());
            throw new RuntimeException("Error al guardar pago: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PagoDto confirmarPago(Long id) {
        log.info("Confirmando pago con id: {}", id);
        return repository.findById(id).map(pago -> {
            pago.setEstado("PAGADO");
            pago.setFechaPago(LocalDateTime.now());
            log.info("Pago confirmado para cliente: {}", pago.getClienteNombre());
            return toDto(repository.save(pago));
        }).orElseThrow(() -> new ResourceNotFoundException("No se puede confirmar, pago no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public Boolean deleteById(Long id) {
        log.info("Eliminando pago con id: {}", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        throw new ResourceNotFoundException("No se encontró pago para eliminar con ID: " + id);
    }
}
