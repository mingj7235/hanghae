package com.hhplus.lecture.domain

import com.hhplus.lecture.infra.repository.LectureRepository
import org.springframework.stereotype.Component

@Component
class LectureManager(
    private val lectureRepository: LectureRepository,
)
