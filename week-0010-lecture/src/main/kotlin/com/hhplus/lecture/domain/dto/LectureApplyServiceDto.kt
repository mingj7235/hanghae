package com.hhplus.lecture.domain.dto

import com.hhplus.lecture.infra.entity.Lecture

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
    ) {
        companion object {
            fun of(studentEntity: com.hhplus.lecture.infra.entity.Student): Student =
                Student(
                    studentId = studentEntity.id,
                    name = studentEntity.name,
                )
        }
    }

    data class Lecture(
        val lectureId: Long,
        val title: String,
        val capacity: Int,
    ) {
        companion object {
            fun of(lectureEntity: com.hhplus.lecture.infra.entity.Lecture): Lecture =
                Lecture(
                    lectureId = lectureEntity.id,
                    title = lectureEntity.title,
                    capacity = lectureEntity.capacity,
                )
        }
    }
}
