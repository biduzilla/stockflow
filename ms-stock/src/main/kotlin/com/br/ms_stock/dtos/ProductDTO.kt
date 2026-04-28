package com.br.ms_stock.dtos

import java.math.BigDecimal
import java.util.*

data class ProductDTO(
    var id: UUID? = null,
    var name: String = "",
    var description: String = "",
    var price: BigDecimal = BigDecimal.ZERO,
)
