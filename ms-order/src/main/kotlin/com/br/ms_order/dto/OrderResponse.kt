package com.br.ms_order.dto

import com.br.ms_order.enums.OrderStatusEnum
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class OrderResponse(
    val id: UUID,
    val customerId: UUID,
    val status: OrderStatusEnum,
    val totalAmount: BigDecimal,
    val items: List<OrderItemResponse>,
    val createdAt: LocalDateTime,
    val cancellationReason: String? = null
)

data class OrderItemResponse(
    val productId: UUID,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val totalPrice: BigDecimal
)
