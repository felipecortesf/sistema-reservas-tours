package com.reservatours.msreportes.service.impl;

import com.reservatours.msreportes.dto.ReporteDto;
import com.reservatours.msreportes.model.Reporte;
import com.reservatours.msreportes.repository.ReporteRepository;
import com.reservatours.msreportes.service.ReporteService;
import lombok.RequiredArgsConstructor;
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

    private Reporte toEntity(ReporteDto dto) {
        return new Reporte(dto.getId(), dto.getTipo(), dto.getFechaReporte(),
                dto.getTotalReservas(), dto.getTotalEmbarques(), dto.getTotalRetrasos(),
                dto.getTotalClientesNoEncontrados(), dto.getResumen(),
                dto.getEnviadoWhatsapp(), dto.getFechaCreacion());
    }

    @Override
    public List<ReporteDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public ReporteDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public ReporteDto findByFecha(String fecha) {
        return repository.findByFechaReporte(LocalDate.parse(fecha))
                .map(this::toDto).orElse(null);
    }

    @Override
    public ReporteDto generarReporteDiario() {
        LocalDate hoy = LocalDate.now();
        String resumen = String.format(
            "📊 Reporte del %s\\n\\n" +
            "✅ Reservas del dia: pendiente\\n" +
            "🚌 Embarques programados: pendiente\\n" +
            "⚠️ Retrasos reportados: 0\\n" +
            "❌ Clientes no encontrados: 0\\n\\n" +
            "Sistema funcionando correctamente.", hoy);

        Reporte reporte = new Reporte(null, "DIARIO", hoy,
                0, 0, 0, 0, resumen, false, LocalDateTime.now());
        return toDto(repository.save(reporte));
    }

    // Envia reporte a Felipe todos los dias a las 22:00
    @Scheduled(cron = "0 0 22 * * *")
    @Override
    public void enviarReporteDiarioWhatsApp() {
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
            repository.findById(reporte.getId()).ifPresent(r -> {
                r.setEnviadoWhatsapp(true);
                repository.save(r);
            });
        } catch (Exception e) {
            System.out.println("Error enviando reporte: " + e.getMessage());
        }
    }
}
