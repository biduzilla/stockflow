package com.br.ms_order.configs

import com.br.shared.configs.BaseSecurityConfiguration
import com.br.shared.configs.CustomAuthenticationEntryPoint
import com.br.shared.security.IJwtService
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    jwtService: IJwtService,
    handlerExceptionResolver: HandlerExceptionResolver,
    customAuthenticationEntryPoint: CustomAuthenticationEntryPoint
) : BaseSecurityConfiguration(jwtService, handlerExceptionResolver, customAuthenticationEntryPoint) {

    override fun configureRoutes(auth: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry) {
        auth
            .requestMatchers("/hello").permitAll()
            .anyRequest().authenticated()
    }
}