package com.hhplus.lecture.infra.repository.implement

import com.hhplus.lecture.application.repository.ApplyHistoryRepository
import com.hhplus.lecture.domain.entity.ApplyHistory
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

    override fun findByStudentId(studentId: Long): List<ApplyHistory> = jpaApplyHistoryRepository.findByStudentId(studentId)

    override fun findAll(): List<ApplyHistory> = jpaApplyHistoryRepository.findAll()

    override fun save(applyHistory: ApplyHistory) {
        jpaApplyHistoryRepository.save(applyHistory)
    }

    override fun deleteAll() {
        jpaApplyHistoryRepository.deleteAll()
    }
}
