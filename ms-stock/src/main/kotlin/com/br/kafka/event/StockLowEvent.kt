package com.br.kafka.event

import java.time.LocalDateTime
import java.util.*

data class StockLowEvent(
    val eventId: UUID = UUID.randomUUID(),
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val productId: UUID,
    val availableQuantity: Long,
)
