package com.br.ms_order

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.br.ms_order", "com.br.shared"])
class MsOrderApplication

fun main(args: Array<String>) {
	runApplication<MsOrderApplication>(*args)
}
