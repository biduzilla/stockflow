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
    @Column(name = "customer_id", nullable = false)
    var customerId: UUID? = null,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatusEnum = OrderStatusEnum.AWAITING_STOCK,
    var totalAmount: BigDecimal = BigDecimal.ZERO,
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(
        mappedBy = "order",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    var items: MutableList<OrderItem> = mutableListOf(),

    @Column(name = "cancellation_reason")
    var cancellationReason: String? = null
) {
    fun calculateTotal(): BigDecimal {
        return items.sumOf { it.totalPrice }
    }
}

