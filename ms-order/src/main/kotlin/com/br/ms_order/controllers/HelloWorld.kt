package com.br.ms_order.controllers

import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hello")
class HelloWorld {
    @GetMapping("/hello")
    fun hello() = "Hello World"

    @GetMapping("/test")
    fun test(auth: Authentication): String {
        println(auth)
        return "ok"
    }
}