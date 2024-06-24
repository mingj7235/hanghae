package com.hhplus.lecture.domain.dto

object LectureApplyServiceDto {
    data class Apply(
        val studentId: Long,
        val lectureId: Long,
    )

    data class ApplyResult(
        val result: Boolean,
    )

    data class Student(
        val studentId: Long,
        val name: String,
    ) {
        companion object {
            fun of(studentEntity: com.hhplus.lecture.infra.entity.Student): Student {
                return Student(
                    studentId = studentEntity.id!!,
                    name = studentEntity.name,
                )
            }
        }
    }
}
