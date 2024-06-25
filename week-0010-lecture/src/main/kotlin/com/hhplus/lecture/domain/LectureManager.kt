package com.hhplus.lecture.domain

import com.hhplus.lecture.common.exception.errors.LectureException
import com.hhplus.lecture.domain.dto.LectureApplyServiceDto
import com.hhplus.lecture.infra.repository.LectureRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class LectureManager(
    private val lectureRepository: LectureRepository,
) {
    /**
     * 1. 현재 시간이 applyStartAt 보다 후인지 검사한다.
     * 2. 현재 시간이 lectureAt 보다 전인지 검사한다.
     */
    fun findAvailableById(lectureId: Long): LectureApplyServiceDto.Lecture {
        val lecture =
            lectureRepository.findById(lectureId)
                ?: throw LectureException.LectureNotfound()

        validateApplyDateTime(
            applyStartAt = lecture.applyStartAt,
            lectureAt = lecture.lectureAt,
        )

        return LectureApplyServiceDto.Lecture.of(lecture)
    }

    private fun validateApplyDateTime(
        applyStartAt: LocalDateTime,
        lectureAt: LocalDateTime,
    ) {
        val now = LocalDateTime.now()
        if (now.isBefore(applyStartAt) || now.isAfter(lectureAt)) throw LectureException.InvalidLectureApplyDateTime()
    }
}
