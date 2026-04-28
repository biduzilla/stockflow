package com.br.ms_order.dto

import java.math.BigDecimal
import java.util.*

data class OrderItemDTO(
    val productId: UUID,
    val quantity: Int,
    val unitPrice: BigDecimal
) {
    fun toEntity(): com.br.ms_order.models.OrderItem {
        return com.br.ms_order.models.OrderItem(
            productId = productId,
            quantity = quantity,
            unitPrice = unitPrice,
            totalPrice = unitPrice.multiply(BigDecimal.valueOf(quantity.toLong()))
        )
    }
}
