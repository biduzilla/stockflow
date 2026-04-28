package com.br.ms_stock.controllers

import com.br.ms_stock.models.Stock
import com.br.ms_stock.services.IStockService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/stocks")
class StockController(
    private val stockService: IStockService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody stock: Stock): Stock {
        return stockService.save(stock)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: UUID): Stock {
        return stockService.findById(id)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        stockService.delete(id)
    }
}