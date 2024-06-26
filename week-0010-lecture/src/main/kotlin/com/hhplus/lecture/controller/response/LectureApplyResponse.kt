package com.hhplus.lecture.controller.response

import com.hhplus.lecture.application.dto.LectureApplyServiceDto

object LectureApplyResponse {
    data class ApplyResult(
        val lectureTitle: String? = null,
        val result: Boolean,
    ) {
        companion object {
            fun of(applyResultDto: LectureApplyServiceDto.ApplyResult): ApplyResult =
                ApplyResult(
                    lectureTitle = applyResultDto.lectureTitle,
                    result = applyResultDto.result,
                )
        }
    }
}
