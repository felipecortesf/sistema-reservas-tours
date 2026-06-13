package com.reservatours.msreservas.kafka;

import com.reservatours.msreservas.dto.ReservaDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReservaEventProducer {

    private static final Logger log = LoggerFactory.getLogger(ReservaEventProducer.class);
    private static final String TOPIC = "reserva.creada";

    private final KafkaTemplate<String, ReservaDto> kafkaTemplate;

    public ReservaEventProducer(KafkaTemplate<String, ReservaDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicarReservaCreada(ReservaDto reserva) {
        log.info("Publicando evento reserva.creada para reserva id: {}", reserva.getId());
        kafkaTemplate.send(TOPIC, reserva.getId().toString(), reserva);
    }
}
