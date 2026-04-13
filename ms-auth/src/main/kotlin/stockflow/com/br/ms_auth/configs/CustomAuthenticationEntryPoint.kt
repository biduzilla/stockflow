package stockflow.com.br.ms_auth.configs

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver
import stockflow.com.br.ms_auth.exceptions.InvalidTokenException

@Component
class CustomAuthenticationEntryPoint(
    private val handlerExceptionResolver: HandlerExceptionResolver
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {

        handlerExceptionResolver.resolveException(
            request,
            response,
            null,
            InvalidTokenException(
                "Token ausente ou inválido"
            )
        )
    }
}