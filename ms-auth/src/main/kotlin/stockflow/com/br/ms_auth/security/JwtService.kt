package stockflow.com.br.ms_auth.security

import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import stockflow.com.br.ms_auth.exceptions.InvalidTokenException
import stockflow.com.br.ms_auth.models.User
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
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
class JwtService(
    @Value($$"${security.jwt.private-key}") private val privateKeyResource: Resource,
    @Value($$"${security.jwt.public-key}") private val publicKeyResource: Resource,
    @Value($$"${security.jwt.expiration-time}") private val jwtExpiration: Long,
    @Value($$"${security.jwt.refresh-expiration-time}") private val refreshExpiration: Long
) : IJwtService {

    private val privateKey: PrivateKey by lazy { loadPrivateKey() }
    private val publicKey: PublicKey by lazy { loadPublicKey() }

    override fun extractUsername(token: String): String =
        extractClaim(token, Claims::getSubject)


    override fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T =
        claimsResolver(extractAllClaims(token))

    override fun generateToken(userDetails: UserDetails): String =
        generateToken(emptyMap(), userDetails)

    override fun generateToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails
    ): String {
        return buildToken(extraClaims, userDetails, getExpirationTime())
    }

    override fun generateRefreshToken(userDetails: UserDetails): String =
        buildToken(
            mapOf("type" to "refresh"),
            userDetails,
            getRefreshExpirationTime()
        )

    override fun getExpirationTime(): Long = jwtExpiration

    override fun getRefreshExpirationTime(): Long = refreshExpiration

    override fun isTokenValid(
        token: String,
        userDetails: UserDetails
    ): Boolean {
        val userName: String = extractUsername(token)
        return userName == userDetails.username && !isTokenExpired(token)
    }

    override fun isRefreshTokenValid(
        token: String,
        userDetails: UserDetails
    ): Boolean {
        val userName = extractUsername(token)
        val tokenType = extractClaim(token) { it["type", String::class.java] }

        return userName == userDetails.username
                && !isTokenExpired(token)
                && tokenType == "refresh"
    }

    private fun buildToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        expirationTime: Long,
    ): String {
        val user = userDetails as User

        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .claim("userId", user.id.toString())
            .claim("name", user.name)
            .claim("roles", user.authorities.map { it.authority })
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact()
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
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: ExpiredJwtException) {
            throw InvalidTokenException("Token expirado")
        } catch (e: MalformedJwtException) {
            throw InvalidTokenException("Token malformado")
        } catch (e: UnsupportedJwtException) {
            throw InvalidTokenException("Token não suportado")
        } catch (e: IllegalArgumentException) {
            throw InvalidTokenException("Token vazio ou nulo")
        } catch (e: JwtException) {
            throw InvalidTokenException("Token inválido")
        }

    private fun loadPrivateKey(): PrivateKey {
        val pem = privateKeyResource.inputStream.bufferedReader().readText()
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("-----BEGIN RSA PRIVATE KEY-----", "")
            .replace("-----END RSA PRIVATE KEY-----", "")
            .replace("\\s".toRegex(), "")
        val keyByte = Base64.getDecoder().decode(pem)
        return KeyFactory.getInstance("RSA")
            .generatePrivate(PKCS8EncodedKeySpec(keyByte))
    }

    private fun loadPublicKey(): PublicKey {
        val pem = publicKeyResource.inputStream.bufferedReader().readText()
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\\s".toRegex(), "")
        val keyBytes = Base64.getDecoder().decode(pem)
        return KeyFactory.getInstance("RSA")
            .generatePublic(X509EncodedKeySpec(keyBytes))
    }
}
