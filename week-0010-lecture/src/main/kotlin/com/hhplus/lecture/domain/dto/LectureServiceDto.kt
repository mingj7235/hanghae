package com.hhplus.lecture.domain.dto

object LectureServiceDto {
    data class Apply(
        val studentId: Long,
        val lectureId: Long,
    )
}
