package com.hhplus.lecture.infra.entity

import com.hhplus.lecture.common.type.ApplyStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class ApplyHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "student_id")
    val student_id: Long,
    @Column(name = "lecture_id")
    val lectureId: Long,
    @Column(name = "apply_status")
    @Enumerated(value = EnumType.STRING)
    val applyStatus: ApplyStatus,
) : BaseEntity()
