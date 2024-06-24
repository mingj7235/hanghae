package com.hhplus.lecture.controller.request

object LectureRequest {
    data class Apply(
        val studentId: Long,
        val lectureId: Long,
    )
}
