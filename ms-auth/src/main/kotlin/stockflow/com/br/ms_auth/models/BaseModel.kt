package stockflow.com.br.ms_auth.models

import jakarta.persistence.Column
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

abstract class BaseModel(
    @field:CreatedDate
    @Column(insertable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    @field:CreatedBy
    @Column(insertable = false)
    var createdBy: String? = null,
    @field:LastModifiedDate
    @Column(insertable = false)
    var updatedAt: LocalDateTime? = null,
    @field:LastModifiedBy
    @Column(insertable = false)
    var updatedBy: String? = null,
    var deleted: Boolean = false
)