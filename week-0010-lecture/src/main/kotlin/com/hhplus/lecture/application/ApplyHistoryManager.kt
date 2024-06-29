package com.hhplus.lecture.application

import com.hhplus.lecture.application.repository.ApplyHistoryRepository
import com.hhplus.lecture.common.exception.errors.ApplyHistoryException
import com.hhplus.lecture.common.type.ApplyStatus
import com.hhplus.lecture.domain.entity.ApplyHistory
import com.hhplus.lecture.domain.entity.Lecture
import com.hhplus.lecture.domain.entity.Student
import org.springframework.stereotype.Component

@Component
class ApplyHistoryManager(
    private val applyHistoryRepository: ApplyHistoryRepository,
) {
    /**
     * 히스토리 내역이 없거나, 히스토리 내역이 있다면 completed 여서는 안된다.
     */
    fun checkApplyStatus(
        studentId: Long,
        lectureId: Long,
    ) {
        if (hasApplied(studentId, lectureId)) {
            throw ApplyHistoryException.AlreadyApplied()
        }
    }

    fun hasApplied(
        studentId: Long,
        lectureId: Long,
    ) = applyHistoryRepository
        .findByStudentIdAndLectureId(studentId, lectureId)
        .any { it.applyStatus == ApplyStatus.COMPLETED }

    fun save(
        student: Student,
        lecture: Lecture,
        applyStatus: ApplyStatus,
    ) {
        applyHistoryRepository.save(
            ApplyHistory(
                student = student,
                lecture = lecture,
                applyStatus = applyStatus,
            ),
        )
    }
}
