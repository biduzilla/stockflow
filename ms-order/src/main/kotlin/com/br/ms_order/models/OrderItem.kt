package com.br.ms_order.models

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "ORDER_ITEM")
data class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    @ManyToOne
    @JoinColumn(name = "productId")
    var productId: UUID? = null,
    var quantity: Int = 1,
    var unitPrice: BigDecimal = BigDecimal.ZERO,
    var totalPrice: BigDecimal = BigDecimal.ZERO,
)
