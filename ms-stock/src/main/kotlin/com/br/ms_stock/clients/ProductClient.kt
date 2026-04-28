package com.br.ms_stock.clients

import com.br.ms_stock.dtos.ProductDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID

@FeignClient(
    name = "product-service",
    url = $$"${product-service.url}"
)
interface ProductClient {
    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: UUID
    ): ProductDTO
}