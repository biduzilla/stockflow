package com.br.ms_order.repositories

import com.br.ms_order.models.OrderItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderItemRepository : JpaRepository<OrderItem, UUID> {
}