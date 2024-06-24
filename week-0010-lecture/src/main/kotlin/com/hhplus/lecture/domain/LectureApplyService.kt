package com.hhplus.lecture.domain

import com.hhplus.lecture.domain.dto.LectureApplyServiceDto
import org.springframework.stereotype.Service

@Service
class LectureApplyService(
    private val lectureManager: LectureManager,
    private val studentManager: StudentManager,
    private val applyHistoryManager: ApplyHistoryManager,
) {
    fun apply(applyDto: LectureApplyServiceDto.Apply): LectureApplyServiceDto.ApplyResult {
        TODO()
    }
}
