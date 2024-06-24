package com.hhplus.lecture.controller.request

import com.hhplus.lecture.domain.dto.LectureServiceDto

object LectureRequest {
    data class Apply(
        val studentId: Long,
        val lectureId: Long,
    ) {
        companion object {
            fun toApplyDto(request: Apply): LectureServiceDto.Apply {
                return LectureServiceDto.Apply(
                    studentId = request.studentId,
                    lectureId = request.lectureId,
                )
            }
        }
    }
}
