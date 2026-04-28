package com.br.ms_order.producers

import com.br.kafka.event.OrderCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class OrderEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, OrderCreatedEvent>
) {
    companion object {
        private const val TOPIC = "pedidos"
        private val logger = LoggerFactory.getLogger(OrderEventProducer::class.java)
    }

    fun publishOrderCreated(event: OrderCreatedEvent) {
        kafkaTemplate.send(TOPIC, event.eventId.toString(), event)
            .whenComplete { _, ex ->
                if (ex == null) {
                    logger.info("Evento pedido.criado publicado: ${event.orderId}")
                } else {
                    logger.error("Falha ao publicar evento: ${event.orderId}", ex)
                }
            }
    }
}