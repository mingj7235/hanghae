package com.hhplus.lecture.infra.repository

import com.hhplus.lecture.infra.entity.Lecture

interface LectureRepository {
    fun findById(lectureId: Long): Lecture?

    fun save(lecture: Lecture)

    fun deleteAll()
}
