package com.br.ms_order.services

import com.br.kafka.event.OrderCreatedEvent
import com.br.kafka.event.OrderItemEvent
import com.br.ms_order.dto.CreateOrderRequest
import com.br.ms_order.dto.OrderItemResponse
import com.br.ms_order.dto.OrderResponse
import com.br.ms_order.enums.OrderStatusEnum
import com.br.ms_order.exceptions.NotFoundException
import com.br.ms_order.models.Order
import com.br.ms_order.producers.OrderEventProducer
import com.br.ms_order.repositories.OrderRepository
import jakarta.transaction.Transactional
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface IOrderService {
    fun createOrder(request: CreateOrderRequest): OrderResponse
    fun findById(id: UUID): OrderResponse
    fun cancelOrder(orderId: UUID, reason: String)
    fun confirmOrder(orderId: UUID)
}

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val eventProducer: OrderEventProducer
) : IOrderService  {
    @Transactional
    override fun createOrder(request: CreateOrderRequest): OrderResponse {
        if (request.customerId == null) {
            throw BadRequestException("Customer ID is required")
        }

        if (request.items.isEmpty()) {
            throw BadRequestException("Order must have at least one item")
        }

        request.items.forEach { item ->
            if (item.quantity <= 0) {
                throw BadRequestException("Quantity must be greater than zero for product ${item.productId}")
            }
            if (item.unitPrice < java.math.BigDecimal.ZERO) {
                throw BadRequestException("Unit price cannot be negative")
            }
        }

        var order = Order(
            customerId = request.customerId,
            status = OrderStatusEnum.AWAITING_STOCK,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        order.items.addAll(request.items.map { itemDTO ->
            itemDTO.toEntity().apply {
                order = this@apply.order!!
            }
        })

        order.totalAmount = order.calculateTotal()

        val savedOrder = orderRepository.save(order)

        eventProducer.publishOrderCreated(
            OrderCreatedEvent(
                orderId = savedOrder.id!!,
                customerId = savedOrder.customerId!!,
                items = savedOrder.items.map {
                    OrderItemEvent(
                        productId = it.productId!!,
                        quantity = it.quantity,
                        unitPrice = it.unitPrice
                    )
                },
                totalAmount = savedOrder.totalAmount,
                status = savedOrder.status.name
            )
        )

        return savedOrder.toResponse()
    }

    override fun findById(id: UUID): OrderResponse {
        val order = orderRepository.findById(id)
            .orElseThrow { NotFoundException("Order not found") }
        return order.toResponse()
    }

    @Transactional
    override fun cancelOrder(orderId: UUID, reason: String) {
        val order = orderRepository.findById(orderId)
            .orElseThrow { NotFoundException("Order not found") }

        if (order.status == OrderStatusEnum.CANCELLED || order.status == OrderStatusEnum.RESERVED) {
            throw BadRequestException("Order with status ${order.status} cannot be modified")
        }

        order.status = OrderStatusEnum.CANCELLED
        order.cancellationReason = reason
        order.updatedAt = LocalDateTime.now()
        orderRepository.save(order)
    }

    @Transactional
    override fun confirmOrder(orderId: UUID) {
        val order = orderRepository.findById(orderId)
            .orElseThrow { NotFoundException("Order not found") }

        if (order.status == OrderStatusEnum.CANCELLED || order.status == OrderStatusEnum.RESERVED) {
            throw BadRequestException("Order with status ${order.status} cannot be modified")
        }

        order.status = OrderStatusEnum.RESERVED // ou CONFIRMED, conforme sua enum
        order.updatedAt = LocalDateTime.now()
        orderRepository.save(order)
    }
}

private fun Order.toResponse(): OrderResponse {
    return OrderResponse(
        id = id!!,
        customerId = customerId!!,
        status = status,
        totalAmount = totalAmount,
        items = items.map {
            OrderItemResponse(
                productId = it.productId!!,
                quantity = it.quantity,
                unitPrice = it.unitPrice,
                totalPrice = it.totalPrice
            )
        },
        createdAt = createdAt,
        cancellationReason = cancellationReason
    )
}