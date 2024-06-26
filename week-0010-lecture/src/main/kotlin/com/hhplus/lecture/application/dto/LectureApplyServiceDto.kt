package com.hhplus.lecture.application.dto

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
        val title: String,
        val capacity: Int,
    )
}
