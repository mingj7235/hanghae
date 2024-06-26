package com.hhplus.lecture.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class BaseEntity {
    @CreatedDate
    @Column(name = "created_datetime", nullable = false, updatable = false)
    open var createdDateTime: LocalDateTime? = null
        protected set

    @LastModifiedDate
    @Column(name = "updated_datetime", nullable = false)
    open var updatedDateTime: LocalDateTime? = null
        protected set
}
