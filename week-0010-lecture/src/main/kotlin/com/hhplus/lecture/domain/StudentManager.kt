package com.hhplus.lecture.domain

import com.hhplus.lecture.domain.dto.LectureApplyServiceDto
import com.hhplus.lecture.infra.repository.StudentRepository
import org.springframework.stereotype.Component

@Component
class StudentManager(
    private val studentRepository: StudentRepository,
) {
    fun findById(studentId: Long): LectureApplyServiceDto.Student {
        TODO()
    }
}
