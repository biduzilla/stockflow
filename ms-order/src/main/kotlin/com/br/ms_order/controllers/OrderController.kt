package com.br.ms_order.controllers

import com.br.ms_order.dto.CreateOrderRequest
import com.br.ms_order.dto.OrderResponse
import com.br.ms_order.services.IOrderService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: IOrderService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateOrderRequest): OrderResponse {
        return orderService.createOrder(request)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): OrderResponse {
        return orderService.findById(id)
    }
}