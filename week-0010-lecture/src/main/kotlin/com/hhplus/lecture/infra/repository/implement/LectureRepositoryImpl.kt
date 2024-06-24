package com.hhplus.lecture.infra.repository.implement

import com.hhplus.lecture.infra.repository.LectureRepository
import com.hhplus.lecture.infra.repository.jpa.JpaLectureRepository
import org.springframework.stereotype.Repository

@Repository
class LectureRepositoryImpl(
    private val jpaLectureRepository: JpaLectureRepository,
) : LectureRepository
