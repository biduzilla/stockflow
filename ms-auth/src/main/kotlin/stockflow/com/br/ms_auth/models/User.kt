package stockflow.com.br.ms_auth.models

import jakarta.persistence.*
import org.hibernate.annotations.SQLRestriction
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import stockflow.com.br.ms_auth.dtos.UserDTO
import stockflow.com.br.ms_auth.enums.RoleEnum
import java.util.*

@Entity
@Table(name = "USERS", uniqueConstraints = [UniqueConstraint(columnNames = ["email", "deleted", "updatedAt"])])
@SQLRestriction("deleted <> true")
data class User(
    @Id
    @GeneratedValue(GenerationType.UUID)
    var id: UUID? = null,
    var name: String,
    var email: String,
    var password: String = "",
    var role: RoleEnum = RoleEnum.CLIENT
) : BaseModel(), UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority(role.authority))

    override fun getPassword(): String? = password
    override fun getUsername(): String = email
}

fun User.toDTO(): UserDTO = UserDTO(
    id = id,
    name = name,
    email = email,
    password = ""
)
