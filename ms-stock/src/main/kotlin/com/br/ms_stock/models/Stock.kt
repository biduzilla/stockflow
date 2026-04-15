package com.br.ms_stock.models

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "STOCK")
data class Stock(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var productId: UUID? = null,
    var availableQuantity: Long = 0,
    var reservedQuantity: Long = 0,
)