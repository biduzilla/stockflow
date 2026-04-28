package com.br.ms_stock.services

import com.br.kafka.event.StockLowEvent
import com.br.ms_stock.clients.ProductClient
import com.br.ms_stock.exceptions.BadRequestException
import com.br.ms_stock.exceptions.NotFoundException
import com.br.ms_stock.models.Stock
import com.br.ms_stock.repositories.StockRepository
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.*

interface IStockService {
    fun findById(id: UUID): Stock
    fun save(stock: Stock): Stock
    fun delete(id: UUID)
}

@Service
class StockService(
    private val stockRepository: StockRepository,
    private val productClient: ProductClient,
    private val kafkaTemplate: KafkaTemplate<String, StockLowEvent>
) : IStockService {
    override fun findById(id: UUID): Stock {
        return stockRepository.findById(id).orElseThrow {
            NotFoundException("Stock not found")
        }
    }

    override fun save(stock: Stock): Stock {
        if (stock.availableQuantity < 0) {
            throw BadRequestException("Stock not available")
        }

        stock.productId?.let { productId ->
            try {
                productClient.findById(productId)
            } catch (e: Exception) {
                throw NotFoundException("Produto com ID $productId não encontrado no catálogo")
            }
        }

        if (stock.availableQuantity < stock.reservedQuantity) {
            val event = StockLowEvent(
                productId = stock.productId!!,
                availableQuantity = stock.availableQuantity,
            )
            kafkaTemplate.send("estoque.baixo", event)
        }
        return stockRepository.save(stock)
    }

    override fun delete(id: UUID) {
        stockRepository.deleteById(id)
    }
}