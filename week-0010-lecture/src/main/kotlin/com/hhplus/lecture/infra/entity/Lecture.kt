package com.hhplus.lecture.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Lecture(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "title", nullable = false)
    val title: String,
    @Column(name = "apply_start_at", nullable = false)
    val applyStartAt: LocalDateTime,
    @Column(name = "lecture_at", nullable = false)
    val lectureAt: LocalDateTime,
    @Column(name = "capacity", nullable = false)
    val capacity: Int,
) : BaseEntity()
