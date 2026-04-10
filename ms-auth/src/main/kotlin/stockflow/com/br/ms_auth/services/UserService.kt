package stockflow.com.br.ms_auth.services

import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import stockflow.com.br.ms_auth.exceptions.BadRequestException
import stockflow.com.br.ms_auth.exceptions.NotFoundException
import stockflow.com.br.ms_auth.models.User
import stockflow.com.br.ms_auth.repositories.UserRepository

interface IUserService {
    fun save(user: User): User
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User
}

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : IUserService {
    @Transactional
    override fun save(user: User): User {
        if (existsByEmail(user.email)) {
            throw BadRequestException("Email already exists")
        }
        verifyPassword(user.password)

        return userRepository.save(user.apply {
            password = passwordEncoder.encode(user.password).orEmpty()
        })
    }

    override fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    override fun findByEmail(email: String): User {
        return userRepository.findByEmail(email).let { user ->
            user ?: throw NotFoundException("User not found")
        }
    }

    private fun verifyPassword(password: String) {
        if (password.length < 8) {
            throw BadRequestException("The password must contain at least 8 digits.")
        }

        if (!password.any { it.isLetter() }) {
            throw BadRequestException("The password must contain one letter.")
        }

        if (!password.any { it.isDigit() }) {
            throw BadRequestException("The password must contain one digit.")
        }
    }
}