package com.hhplus.lecture.application.dto

import com.hhplus.lecture.common.type.LectureStatus
import java.time.LocalDateTime

object LectureApplyServiceDto {
    data class Apply(
        val studentId: Long,
        val lectureId: Long,
    )

    data class ApplyResult(
        val result: Boolean,
        val lectureTitle: String? = null,
    )

    data class Student(
        val studentId: Long,
        val name: String,
    )

    data class Lecture(
        val lectureId: Long,
        val lectureTitle: String,
        val capacity: Int,
        val currentEnrollmentCount: Int,
        val lectureAt: LocalDateTime,
        val lectureStatus: LectureStatus,
    ) {
        companion object {
            fun of(lectureEntity: com.hhplus.lecture.domain.entity.Lecture): Lecture =
                Lecture(
                    lectureId = lectureEntity.id,
                    lectureTitle = lectureEntity.title,
                    capacity = lectureEntity.capacity,
                    currentEnrollmentCount = lectureEntity.currentEnrollmentCount,
                    lectureAt = lectureEntity.lectureAt,
                    lectureStatus =
                        if (lectureEntity.isEnrollmentFull()) {
                            LectureStatus.CLOSED
                        } else {
                            LectureStatus.OPENED
                        },
                )
        }
    }
}
