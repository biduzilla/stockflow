package com.br.ms_stock.repositories

import com.br.ms_stock.models.Stock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StockRepository : JpaRepository<Stock, UUID> {
}