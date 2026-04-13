package com.br.ms_order

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.br"])
class MsOrderApplication

fun main(args: Array<String>) {
	runApplication<MsOrderApplication>(*args)
}
