package com.br.ms_order.consumers

import com.br.kafka.event.StockInsufficientEvent
import com.br.kafka.event.StockReservedEvent
import com.br.ms_order.services.IOrderService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class StockEventConsumer(
    private val orderService: IOrderService
) {
    companion object {
        private val logger = LoggerFactory.getLogger(StockEventConsumer::class.java)
    }

    @KafkaListener(
        topics = ["estoque.insuficiente"],
        groupId = $$"${spring.kafka.consumer.group-id:order-service-group}"
    )
    fun handleStockInsufficient(event: StockInsufficientEvent) {
        logger.info("📩 Recebido estoque.insuficiente: ${event.eventId} | Pedido: ${event.orderId}")
        try {
            orderService.cancelOrder(event.orderId, event.reason)
            logger.info("✅ Pedido ${event.orderId} cancelado com sucesso")
        } catch (e: Exception) {
            logger.error("Erro ao cancelar pedido ${event.orderId}", e)
        }
    }

    @KafkaListener(topics = ["estoque.reservado"], groupId = "\${spring.kafka.consumer.group-id:order-service-group}")
    fun handleStockReserved(event: StockReservedEvent) {
        logger.info("📩 Recebido estoque.reservado: ${event.eventId} | Pedido: ${event.orderId}")
        try {
            orderService.confirmOrder(event.orderId)
            logger.info("✅ Pedido ${event.orderId} confirmado com sucesso")
        } catch (e: Exception) {
            logger.error("Erro ao confirmar pedido ${event.orderId}", e)
        }
    }
}