package com.br.ms_order.models

import com.br.ms_order.enums.OrderStatusEnum
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "ORDERS")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var customerId: UUID? = null,
    var status: OrderStatusEnum = OrderStatusEnum.AWAITING_STOCK,
    var totalAmount: BigDecimal = BigDecimal.ZERO,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updateddAt: LocalDateTime = LocalDateTime.now(),
)
