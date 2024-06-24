package com.hhplus.lecture.controller.response

import com.hhplus.lecture.domain.dto.LectureApplyServiceDto

object LectureApplyResponse {
    data class ApplyResult(
        val result: Boolean,
    ) {
        companion object {
            fun of(applyResultDto: LectureApplyServiceDto.ApplyResult): ApplyResult {
                return ApplyResult(
                    result = applyResultDto.result,
                )
            }
        }
    }
}
