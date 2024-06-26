package com.hhplus.lecture.infra.repository.jpa

import com.hhplus.lecture.domain.entity.ApplyHistory
import org.springframework.data.jpa.repository.JpaRepository

interface JpaApplyHistoryRepository : JpaRepository<ApplyHistory, Long> {
    fun findByStudentIdAndLectureId(
        studentId: Long,
        lectureId: Long,
    ): List<ApplyHistory>

    fun findByStudentId(studentId: Long): List<ApplyHistory>
}
