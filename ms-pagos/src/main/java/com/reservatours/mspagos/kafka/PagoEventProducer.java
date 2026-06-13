package com.reservatours.mspagos.kafka;

import com.reservatours.mspagos.dto.PagoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PagoEventProducer {

    private static final Logger log = LoggerFactory.getLogger(PagoEventProducer.class);
    private static final String TOPIC = "pago.confirmado";

    private final KafkaTemplate<String, PagoDto> kafkaTemplate;

    public PagoEventProducer(KafkaTemplate<String, PagoDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicarPagoConfirmado(PagoDto pago) {
        log.info("Publicando evento pago.confirmado para pago id: {}", pago.getId());
        kafkaTemplate.send(TOPIC, pago.getId().toString(), pago);
    }
}
