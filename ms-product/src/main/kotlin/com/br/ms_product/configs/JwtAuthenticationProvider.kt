package com.br.ms_product.configs

import com.br.ms_order.exceptions.InvalidTokenException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider : AuthenticationProvider {
    override fun authenticate(authentication: Authentication): Authentication? {
        if (authentication is UsernamePasswordAuthenticationToken
            && authentication.isAuthenticated
        ) {
            return authentication
        }
        throw InvalidTokenException("Autenticação não suportada")
    }

    override fun supports(authentication: Class<*>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}