package com.hhplus.lecture.infra.repository

import com.hhplus.lecture.infra.entity.Lecture
import java.time.LocalDateTime

interface LectureRepository {
    fun findAvailableById(
        lectureId: Long,
        currentDateTime: LocalDateTime,
    ): Lecture?

    fun deleteAll()
}
