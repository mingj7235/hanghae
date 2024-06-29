package com.hhplus.lecture.application.repository

import com.hhplus.lecture.domain.entity.Lecture

interface LectureRepository {
    fun findByLectureIdWithPessimisticLock(lectureId: Long): Lecture?

    fun findById(lectureId: Long): Lecture?

    fun findAll(): List<Lecture>

    fun save(lecture: Lecture): Lecture

    fun deleteAll()
}
