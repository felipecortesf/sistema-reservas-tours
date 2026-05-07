package com.reservatours.msreportes.service.impl;

import com.reservatours.msreportes.dto.ReporteDto;
import com.reservatours.msreportes.model.Reporte;
import com.reservatours.msreportes.repository.ReporteRepository;
import com.reservatours.msreportes.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private static final Logger log = LoggerFactory.getLogger(ReporteServiceImpl.class);
    private final ReporteRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${whatsapp.token}")
    private String whatsappToken;

    @Value("${whatsapp.phone.number.id}")
    private String phoneNumberId;

    @Value("${telefono.felipe}")
    private String telefonoFelipe;

    private ReporteDto toDto(Reporte r) {
        return new ReporteDto(r.getId(), r.getTipo(), r.getFechaReporte(),
                r.getTotalReservas(), r.getTotalEmbarques(), r.getTotalRetrasos(),
                r.getTotalClientesNoEncontrados(), r.getResumen(),
                r.getEnviadoWhatsapp(), r.getFechaCreacion());
    }

    @Override
    public List<ReporteDto> findAll() {
        log.info("Consultando todos los reportes");
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public ReporteDto findById(Long id) {
        log.info("Buscando reporte con id: {}", id);
        return repository.findById(id).map(this::toDto).orElseGet(() -> {
            log.warn("Reporte no encontrado con id: {}", id);
            return null;
        });
    }

    @Override
    public ReporteDto findByFecha(String fecha) {
        log.info("Buscando reporte para fecha: {}", fecha);
        return repository.findByFechaReporte(LocalDate.parse(fecha)).map(this::toDto).orElseGet(() -> {
            log.warn("Reporte no encontrado para fecha: {}", fecha);
            return null;
        });
    }

    @Override
    public ReporteDto generarReporteDiario() {
        LocalDate hoy = LocalDate.now();
        log.info("Generando reporte diario para: {}", hoy);
        String resumen = String.format(
            "Reporte del %s - Sistema funcionando correctamente.", hoy);
        Reporte reporte = new Reporte(null, "DIARIO", hoy,
                0, 0, 0, 0, resumen, false, LocalDateTime.now());
        ReporteDto saved = toDto(repository.save(reporte));
        log.info("Reporte diario generado con id: {}", saved.getId());
        return saved;
    }

    @Scheduled(cron = "0 0 22 * * *")
    @Override
    public void enviarReporteDiarioWhatsApp() {
        log.info("Enviando reporte diario a Felipe por WhatsApp");
        ReporteDto reporte = generarReporteDiario();
        String url = "https://graph.facebook.com/v25.0/" + phoneNumberId + "/messages";
        String mensaje = String.format(
            "{\"messaging_product\":\"whatsapp\",\"to\":\"%s\",\"type\":\"text\",\"text\":{\"body\":\"%s\"}}",
            telefonoFelipe, reporte.getResumen()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(whatsappToken);
        try {
            restTemplate.postForEntity(url, new HttpEntity<>(mensaje, headers), String.class);
            log.info("Reporte enviado exitosamente a: {}", telefonoFelipe);
            repository.findById(reporte.getId()).ifPresent(r -> {
                r.setEnviadoWhatsapp(true);
                repository.save(r);
            });
        } catch (Exception e) {
            log.error("Error enviando reporte: {}", e.getMessage());
        }
    }
}
