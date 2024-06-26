package com.hhplus.lecture.controller.response

import com.hhplus.lecture.application.dto.LectureApplyServiceDto
import com.hhplus.lecture.common.type.LectureStatus
import java.time.LocalDateTime

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

    data class Lecture(
        val lectureId: Long,
        val lectureTitle: String,
        val capacity: Int,
        val currentEnrollmentCount: Int,
        val lectureAt: LocalDateTime,
        val lectureStatus: LectureStatus,
    ) {
        companion object {
            fun of(lectureDto: LectureApplyServiceDto.Lecture): Lecture =
                Lecture(
                    lectureId = lectureDto.lectureId,
                    lectureTitle = lectureDto.lectureTitle,
                    capacity = lectureDto.capacity,
                    currentEnrollmentCount = lectureDto.currentEnrollmentCount,
                    lectureAt = lectureDto.lectureAt,
                    lectureStatus = lectureDto.lectureStatus,
                )
        }
    }
}
