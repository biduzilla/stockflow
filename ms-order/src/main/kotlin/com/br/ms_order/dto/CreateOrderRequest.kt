package com.br.ms_order.dto

import java.util.*

data class CreateOrderRequest(
    val customerId: UUID,
    val items: List<OrderItemDTO>
)
