package com.br.ms_stock.models

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

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