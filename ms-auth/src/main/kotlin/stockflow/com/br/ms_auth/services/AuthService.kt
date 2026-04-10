package stockflow.com.br.ms_auth.services

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import stockflow.com.br.ms_auth.dtos.AuthResponse
import stockflow.com.br.ms_auth.dtos.LoginRequest
import stockflow.com.br.ms_auth.exceptions.BadCredentialsException
import stockflow.com.br.ms_auth.exceptions.BadRequestException
import stockflow.com.br.ms_auth.security.IJwtService

interface IAuthService {
    fun authenticate(request: LoginRequest): AuthResponse
    fun refreshToken(refreshToken: String): AuthResponse
}

@Service
class AuthService(
    private val jwtService: IJwtService,
    private val userService: IUserService,
    private val authenticationManager: AuthenticationManager,
) : IAuthService {
    override fun authenticate(request: LoginRequest): AuthResponse {
        return try {
            val auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    request.login,
                    request.password
                )
            )

            val userDetails = auth.principal as UserDetails
            val accessToken = jwtService.generateToken(userDetails)
            val refreshToken = jwtService.generateRefreshToken(userDetails)

            AuthResponse(
                accessToken = accessToken,
                refreshToken = refreshToken,
                expiresIn = jwtService.getExpirationTime()
            )
        } catch (e: AuthenticationException) {
            throw BadCredentialsException("Invalid email or password")
        }
    }

    override fun refreshToken(refreshToken: String): AuthResponse {
        val username = jwtService.extractUsername(refreshToken)
        val user = userService.findByEmail(username)

        if (!jwtService.isRefreshTokenValid(refreshToken, user)) {
            throw BadRequestException("Invalid refresh token")
        }

        val newAccessToken = jwtService.generateToken(user)
        val newRefreshToken = jwtService.generateRefreshToken(user)

        return AuthResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            expiresIn = jwtService.getExpirationTime()
        )
    }
}