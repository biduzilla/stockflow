package com.br.shared.configs

import com.br.shared.security.IJwtService
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.HandlerExceptionResolver

abstract class BaseSecurityConfiguration(
    private val jwtService: IJwtService,
    private val handlerExceptionResolver: HandlerExceptionResolver,
    private val customAuthenticationEntryPoint: CustomAuthenticationEntryPoint
) {
    abstract fun configureRoutes(auth: org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry)

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        val filter = JwtAuthenticationFilter(jwtService, handlerExceptionResolver)

        http.run {
            csrf { it.disable() }
            authorizeHttpRequests { configureRoutes(it) }
            sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            exceptionHandling { exception ->
                exception.authenticationEntryPoint(customAuthenticationEntryPoint)
            }
            addFilterBefore(filter, UsernamePasswordAuthenticationFilter::class.java)
        }

        return http.build()
    }
}