package com.hhplus.lecture.controller.response

import com.hhplus.lecture.domain.dto.LectureApplyServiceDto

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
