package stockflow.com.br.ms_auth.models

import java.time.LocalDateTime

abstract class BaseModel(
    var createdAt: LocalDateTime = LocalDateTime.now(),
    var createdBy: String? = null,
    var updatedAt: LocalDateTime? = null,
    var updatedBy: String? = null,
    var deleted: Boolean = false
)