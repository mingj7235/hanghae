package com.hhplus.lecture.application.repository

import com.hhplus.lecture.domain.entity.ApplyHistory

interface ApplyHistoryRepository {
    fun findByStudentIdAndLectureId(
        studentId: Long,
        lectureId: Long,
    ): List<ApplyHistory>

    fun findByStudentId(studentId: Long): List<ApplyHistory>

    fun findAll(): List<ApplyHistory>

    fun save(applyHistory: ApplyHistory)

    fun deleteAll()
}
