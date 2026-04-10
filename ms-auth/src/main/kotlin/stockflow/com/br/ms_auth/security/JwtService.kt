package stockflow.com.br.ms_auth.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

interface IJwtService {
    fun extractUsername(token: String): String
    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T
    fun generateToken(userDetails: UserDetails): String
    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String
    fun getExpirationTime(): Long
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean
}

@Service
class JwtService : IJwtService {
    @Value($$"${security.jwt.secret-key}")
    private lateinit var secretKey: String

    @Value($$"${security.jwt.expiration-time}")
    private var jwtExpiration: Long = 0

    override fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
return claimsResolver(extractClaim())
    }

    override fun generateToken(userDetails: UserDetails): String {
        TODO("Not yet implemented")
    }

    override fun generateToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails
    ): String {
        TODO("Not yet implemented")
    }

    override fun getExpirationTime(): Long {
        TODO("Not yet implemented")
    }

    override fun isTokenValid(
        token: String,
        userDetails: UserDetails
    ): Boolean {
        TODO("Not yet implemented")
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
        Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJwt(token)
            .body

}
