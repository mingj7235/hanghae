package com.hhplus.lecture.infra.repository.implement

import com.hhplus.lecture.infra.entity.Lecture
import com.hhplus.lecture.infra.repository.LectureRepository
import com.hhplus.lecture.infra.repository.jpa.JpaLectureRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class LectureRepositoryImpl(
    private val jpaLectureRepository: JpaLectureRepository,
    @PersistenceContext
    private val entityManager: EntityManager,
) : LectureRepository {
    override fun findAvailableById(
        lectureId: Long,
        currentDateTime: LocalDateTime,
    ): Lecture? {
        val query =
            entityManager.createQuery(
                """
            SELECT l FROM Lecture l 
            WHERE l.id = :lectureId 
              AND :currentDateTime BETWEEN l.applyStartAt AND l.lectureAt
        """,
                Lecture::class.java,
            )
        query.setParameter("lectureId", lectureId)
        query.setParameter("currentDateTime", currentDateTime)

        return query.resultList.firstOrNull()
    }

    override fun deleteAll() {
        jpaLectureRepository.deleteAll()
    }
}
