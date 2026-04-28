package com.br.kafka.event

import java.time.LocalDateTime
import java.util.*

data class StockInsufficientEvent(
    val eventId: UUID = UUID.randomUUID(),
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val orderId: UUID,
    val productId: UUID,
    val requestedQuantity: Long,
    val availableQuantity: Long,
    val reason: String
)

data class StockReservedEvent(
    val eventId: UUID = UUID.randomUUID(),
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val orderId: UUID,
    val productId: UUID,
    val reservedQuantity: Long,
    val reservationId: UUID? = null
)