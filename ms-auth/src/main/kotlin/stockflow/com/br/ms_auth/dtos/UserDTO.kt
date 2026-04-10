package stockflow.com.br.ms_auth.dtos

import stockflow.com.br.ms_auth.models.User
import java.util.*

data class UserDTO(
    var id: UUID? = null,
    var name: String,
    var email: String,
)

fun UserDTO.toModel(): User = User(
    id = id,
    name = name,
    email = email,
)
