package com.hhplus.lecture.domain

import com.hhplus.lecture.common.type.ApplyStatus
import com.hhplus.lecture.domain.dto.LectureApplyServiceDto
import com.hhplus.lecture.infra.entity.Lecture
import com.hhplus.lecture.infra.entity.Student
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LectureApplyService(
    private val lectureManager: LectureManager,
    private val studentManager: StudentManager,
    private val applyHistoryManager: ApplyHistoryManager,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun apply(applyDto: LectureApplyServiceDto.Apply): LectureApplyServiceDto.ApplyResult {
        val student = studentManager.findById(applyDto.studentId)
        val lecture = lectureManager.findAvailableById(applyDto.lectureId)

        return runCatching {
            validateLectureApplication(studentId = student.id, lecture = lecture)
            applyLecture(student = student, lecture = lecture)
            LectureApplyServiceDto.ApplyResult(
                lectureTitle = lecture.title,
                result = true,
            )
        }.getOrElse { e ->
            logger.info(e.message)
            saveApplyFailedHistory(student = student, lecture = lecture)
            LectureApplyServiceDto.ApplyResult(result = false)
        }
    }

    /**
     * 다음 두가지를 검증한다.
     *  1. 학생이 강의를 이미 등록했는지 확인한다.
     *  2. 현재 강의의 수강생이 꽉 찼는지 확인한다.
     */
    private fun validateLectureApplication(
        studentId: Long,
        lecture: Lecture,
    ) {
        applyHistoryManager.checkApplyStatus(
            studentId = studentId,
            lectureId = lecture.id,
        )
        lectureManager.checkCurrentEnrollmentCount(lecture)
    }

    /**
     * 선착순으로 강의 신청을 한다.
     *  1. lecture 의 currentEnrollmentCount 을 1 증가 시킨다.
     *  2. history 에 completed 로 저장을 시킨다.
     */
    private fun applyLecture(
        student: Student,
        lecture: Lecture,
    ) {
        lecture.increaseCurrentEnrollmentCount()
        applyHistoryManager.save(
            student = student,
            lecture = lecture,
            applyStatus = ApplyStatus.COMPLETED,
        )
    }

    /**
     * 강의 신청이 실패해서 history 에 저장되는 경우는 다음과 같다.
     *     1. 이미 학생이 강의를 등록했는데 중복해서 강의 수강 신청을 하는 경우
     *     2. 선착순에 들지 못해서 강의신청을 하지 못한 경우
     */
    private fun saveApplyFailedHistory(
        student: Student,
        lecture: Lecture,
    ) {
        applyHistoryManager.save(
            student = student,
            lecture = lecture,
            applyStatus = ApplyStatus.FAILED,
        )
    }
}
