package com.hhplus.lecture.infra.repository.implement

import com.hhplus.lecture.application.repository.LectureRepository
import com.hhplus.lecture.domain.entity.Lecture
import com.hhplus.lecture.infra.repository.jpa.JpaLectureRepository
import org.springframework.stereotype.Repository

@Repository
class LectureRepositoryImpl(
    private val jpaLectureRepository: JpaLectureRepository,
) : LectureRepository {
    override fun findByLectureIdWithPessimisticLock(lectureId: Long): Lecture? =
        jpaLectureRepository.findByLectureIdWithPessimisticLock(lectureId)

    override fun findAll(): List<Lecture> = jpaLectureRepository.findAll()

    override fun save(lecture: Lecture): Lecture = jpaLectureRepository.save(lecture)

    override fun deleteAll() {
        jpaLectureRepository.deleteAll()
    }
}
