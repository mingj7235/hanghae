package com.hhplus.lecture.application

import com.hhplus.lecture.application.repository.LectureRepository
import com.hhplus.lecture.common.exception.errors.LectureException
import com.hhplus.lecture.domain.entity.Lecture
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class LectureManager(
    private val lectureRepository: LectureRepository,
) {
    fun findAvailableById(lectureId: Long): Lecture {
        val lecture =
            lectureRepository.findByLectureIdWithPessimisticLock(lectureId)
                ?: throw LectureException.LectureNotfound()

        validate(
            applyStartAt = lecture.applyStartAt,
            lectureAt = lecture.lectureAt,
        )

        return lecture
    }

    /**
     * 현재 강의의 정원이 꽉 찼는지 확인한다.
     */
    fun checkCurrentEnrollmentCount(lecture: Lecture) {
        if (lecture.isEnrollmentFull()) throw LectureException.EnrollmentFull()
    }

    /**
     * 1. 현재 시간이 applyStartAt 보다 후인지 검사한다.
     * 2. 현재 시간이 lectureAt 보다 전인지 검사한다.
     */
    private fun validate(
        applyStartAt: LocalDateTime,
        lectureAt: LocalDateTime,
    ) {
        val now = LocalDateTime.now()
        if (now.isBefore(applyStartAt) || now.isAfter(lectureAt)) throw LectureException.InvalidLectureApplyDateTime()
    }

    /**
     * 아직 실제 강의가 시작하지 않은 강의들만 찾아서 리턴한다.
     */
    fun findUpcomingLectures(): List<Lecture> =
        lectureRepository.findAll().filter {
            it.lectureAt.isAfter(LocalDateTime.now())
        }
}
