package com.hhplus.lecture.domain

import com.hhplus.lecture.common.exception.errors.ApplyHistoryException
import com.hhplus.lecture.common.type.ApplyStatus
import com.hhplus.lecture.infra.repository.ApplyHistoryRepository
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
        if (applyHistoryRepository
                .findByStudentIdAndLectureId(studentId, lectureId)
                .any { it.applyStatus == ApplyStatus.COMPLETED }
        ) {
            throw ApplyHistoryException.AlreadyApplied()
        }
    }
}
