package com.hhplus.lecture.infra.repository.jpa

import com.hhplus.lecture.infra.entity.ApplyHistory
import org.springframework.data.jpa.repository.JpaRepository

interface JpaApplyHistoryRepository : JpaRepository<ApplyHistory, Long> {
    fun findByStudentIdAndLectureId(
        studentId: Long,
        lectureId: Long,
    ): List<ApplyHistory>
}
