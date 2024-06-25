package com.hhplus.lecture.infra.repository.implement

import com.hhplus.lecture.infra.repository.ApplyHistoryRepository
import com.hhplus.lecture.infra.repository.jpa.JpaApplyHistoryRepository
import org.springframework.stereotype.Repository

@Repository
class ApplyHistoryRepositoryImpl(
    private val jpaApplyHistoryRepository: JpaApplyHistoryRepository,
) : ApplyHistoryRepository {
    override fun findByStudentIdAndLectureId(
        studentId: Long,
        lectureId: Long,
    ) = jpaApplyHistoryRepository.findByStudentIdAndLectureId(
        studentId = studentId,
        lectureId = lectureId,
    )

    override fun deleteAll() {
        jpaApplyHistoryRepository.deleteAll()
    }
}
