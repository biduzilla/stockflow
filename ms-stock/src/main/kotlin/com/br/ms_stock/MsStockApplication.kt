package com.br.ms_stock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class MsStockApplication

fun main(args: Array<String>) {
    runApplication<MsStockApplication>(*args)
}
