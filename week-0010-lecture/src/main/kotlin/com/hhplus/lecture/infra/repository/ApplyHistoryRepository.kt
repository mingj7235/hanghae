package com.hhplus.lecture.infra.repository

import com.hhplus.lecture.infra.entity.ApplyHistory

interface ApplyHistoryRepository {
    fun findByStudentIdAndLectureId(
        studentId: Long,
        lectureId: Long,
    ): List<ApplyHistory>

    fun deleteAll()
}
