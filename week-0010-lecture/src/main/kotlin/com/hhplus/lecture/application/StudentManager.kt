package com.hhplus.lecture.application

import com.hhplus.lecture.application.repository.StudentRepository
import com.hhplus.lecture.common.exception.errors.StudentException
import com.hhplus.lecture.domain.entity.Student
import org.springframework.stereotype.Component

@Component
class StudentManager(
    private val studentRepository: StudentRepository,
) {
    fun findById(studentId: Long): Student =
        studentRepository.findById(studentId)
            ?: throw StudentException.StudentNotfound()
}
