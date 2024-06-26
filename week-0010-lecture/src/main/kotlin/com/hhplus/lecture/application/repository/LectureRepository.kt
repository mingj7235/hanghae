package com.hhplus.lecture.application.repository

import com.hhplus.lecture.domain.entity.Lecture

interface LectureRepository {
    fun findById(lectureId: Long): Lecture?

    fun save(lecture: Lecture)

    fun deleteAll()
}
