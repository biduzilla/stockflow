package com.br.ms_order.dto

import java.util.UUID

data class CreateOrderRequest(
    val customerId: UUID,
    val items: List<OrderItemDTO>
)
