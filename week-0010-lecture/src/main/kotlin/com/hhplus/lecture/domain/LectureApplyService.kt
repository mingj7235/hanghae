package com.hhplus.lecture.domain

import com.hhplus.lecture.domain.dto.LectureServiceDto
import org.springframework.stereotype.Service

@Service
class LectureApplyService(
    private val lectureManager: LectureManager,
    private val studentManager: StudentManager,
) {
    fun apply(applyDto: LectureServiceDto.Apply) {
    }
}
