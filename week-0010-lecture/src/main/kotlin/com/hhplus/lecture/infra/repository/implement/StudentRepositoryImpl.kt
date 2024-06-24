package com.hhplus.lecture.infra.repository.implement

import com.hhplus.lecture.infra.entity.Student
import com.hhplus.lecture.infra.repository.StudentRepository
import com.hhplus.lecture.infra.repository.jpa.JpaStudentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class StudentRepositoryImpl(
    private val jpaStudentRepository: JpaStudentRepository,
) : StudentRepository {
    override fun findById(studentId: Long): Student? {
        return jpaStudentRepository.findByIdOrNull(studentId)
    }

    override fun deleteAll() {
        jpaStudentRepository.deleteAll()
    }
}
