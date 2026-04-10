package stockflow.com.br.ms_auth.security

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import stockflow.com.br.ms_auth.exceptions.InvalidTokenException
import java.security.Key
import java.security.SignatureException
import java.util.*

interface IJwtService {
    fun extractUsername(token: String): String
    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T
    fun generateToken(userDetails: UserDetails): String
    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String
    fun generateRefreshToken(userDetails: UserDetails): String
    fun getExpirationTime(): Long
    fun getRefreshExpirationTime(): Long
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean
    fun isRefreshTokenValid(token: String, userDetails: UserDetails): Boolean
}

@Service
class JwtService : IJwtService {
    @Value($$"${security.jwt.secret-key}")
    private lateinit var secretKey: String

    @Value($$"${security.jwt.expiration-time}")
    private var jwtExpiration: Long = 0

    @Value("\${security.jwt.refresh-expiration-time}")
    private var refreshExpiration: Long = 86400000

    override fun extractUsername(token: String): String {
        return extractClaim(
            token,
            Claims::getSubject
        )
    }

    override fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        return claimsResolver(extractAllClaims(token))
    }

    override fun generateToken(userDetails: UserDetails): String {
        return generateToken(emptyMap(), userDetails)
    }

    override fun generateToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails
    ): String {
        return buildToken(extraClaims, userDetails, getExpirationTime())
    }

    override fun generateRefreshToken(userDetails: UserDetails): String {
        return buildToken(
            mapOf("type" to "refresh"),
            userDetails,
            getRefreshExpirationTime()
        )
    }

    override fun getExpirationTime(): Long = jwtExpiration

    override fun getRefreshExpirationTime(): Long = refreshExpiration

    override fun isTokenValid(
        token: String,
        userDetails: UserDetails
    ): Boolean {
        val userName: String = userDetails.username
        return (userName == userDetails.username) && isTokenExpired(token)
    }

    override fun isRefreshTokenValid(
        token: String,
        userDetails: UserDetails
    ): Boolean {
        val userName = extractUsername(token)
        val tokenType = extractClaim(token) { claims ->
            claims.get("type", String::class.java)
        }

        return userName == userDetails.username
                && isTokenExpired(tokenType)
                && tokenType == "refresh"
    }

    private fun buildToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        expirationTime: Long,
    ): String = Jwts.builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.username)
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + expirationTime))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact()

    private fun getSignInKey(): Key {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun isTokenExpired(token: String): Boolean =
        extractExpiration(token).before(Date())

    private fun extractAllClaims(token: String): Claims =
        try {
            Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: SignatureException) {
            throw InvalidTokenException("Token signature is invalid")
        } catch (e: ExpiredJwtException) {
            throw InvalidTokenException("Token has expired")
        } catch (e: MalformedJwtException) {
            throw InvalidTokenException("Token is malformed")
        } catch (e: UnsupportedJwtException) {
            throw InvalidTokenException("Token is unsupported")
        } catch (e: IllegalArgumentException) {
            throw InvalidTokenException("Token is empty or null")
        }

}
