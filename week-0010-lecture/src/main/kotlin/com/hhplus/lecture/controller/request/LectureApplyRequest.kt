package com.hhplus.lecture.controller.request

import com.hhplus.lecture.application.dto.LectureApplyServiceDto

object LectureApplyRequest {
    data class Apply(
        val studentId: Long,
        val lectureId: Long,
    ) {
        companion object {
            fun toApplyDto(request: Apply): LectureApplyServiceDto.Apply =
                LectureApplyServiceDto.Apply(
                    studentId = request.studentId,
                    lectureId = request.lectureId,
                )
        }
    }
}
