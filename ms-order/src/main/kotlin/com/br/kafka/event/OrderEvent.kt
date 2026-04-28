package com.br.kafka.event

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class OrderCreatedEvent(
    val eventId: UUID = UUID.randomUUID(),
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val orderId: UUID,
    val customerId: UUID,
    val items: List<OrderItemEvent>,
    val totalAmount: BigDecimal,
    val status: String
)

data class OrderItemEvent(
    val productId: UUID,
    val quantity: Int,
    val unitPrice: BigDecimal
)
