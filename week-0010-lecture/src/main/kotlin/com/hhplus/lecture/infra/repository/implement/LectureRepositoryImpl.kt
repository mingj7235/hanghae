package com.hhplus.lecture.infra.repository.implement

import com.hhplus.lecture.application.repository.LectureRepository
import com.hhplus.lecture.domain.entity.Lecture
import com.hhplus.lecture.infra.repository.jpa.JpaLectureRepository
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class LectureRepositoryImpl(
    private val jpaLectureRepository: JpaLectureRepository,
) : LectureRepository {
    override fun findById(lectureId: Long): Lecture? = jpaLectureRepository.findById(lectureId).getOrNull()

    override fun findAll(): List<Lecture> = jpaLectureRepository.findAll()

    override fun save(lecture: Lecture) {
        jpaLectureRepository.save(lecture)
    }

    override fun deleteAll() {
        jpaLectureRepository.deleteAll()
    }
}
