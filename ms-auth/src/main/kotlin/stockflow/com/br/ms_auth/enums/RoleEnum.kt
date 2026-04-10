package stockflow.com.br.ms_auth.enums

enum class RoleEnum {
    ADMIN, CLIENT;

    val authority: String
        get() = "ROLE_${name}"
}