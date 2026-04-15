package com.br.ms_product.models

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "PRODUCTS")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var name:String="",
    var description:String="",
    var price: BigDecimal = BigDecimal.ZERO,
)
