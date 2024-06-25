package com.hhplus.lecture.infra.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Lecture(
    title: String,
    applyStartAt: LocalDateTime,
    lectureAt: LocalDateTime,
    capacity: Int,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "title", nullable = false)
    var title: String = title
        protected set

    @Column(name = "apply_start_at", nullable = false)
    var applyStartAt: LocalDateTime = applyStartAt
        protected set

    @Column(name = "lecture_at", nullable = false)
    var lectureAt: LocalDateTime = lectureAt
        protected set

    @Column(name = "capacity", nullable = false)
    var capacity: Int = capacity
        protected set

    @Column(name = "capacity", nullable = false)
    var currentEnrollmentCount: Int = 0

    fun increaseCurrentEnrollmentCount() {
        this.currentEnrollmentCount++
    }

    fun isEnrollmentFull(): Boolean = currentEnrollmentCount == capacity
}
