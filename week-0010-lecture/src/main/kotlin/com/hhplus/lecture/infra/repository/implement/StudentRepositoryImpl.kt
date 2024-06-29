package com.hhplus.lecture.infra.repository.implement

import com.hhplus.lecture.application.repository.StudentRepository
import com.hhplus.lecture.domain.entity.Student
import com.hhplus.lecture.infra.repository.jpa.JpaStudentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class StudentRepositoryImpl(
    private val jpaStudentRepository: JpaStudentRepository,
) : StudentRepository {
    override fun save(student: Student): Student = jpaStudentRepository.save(student)

    override fun findById(studentId: Long): Student? = jpaStudentRepository.findByIdOrNull(studentId)

    override fun deleteAll() {
        jpaStudentRepository.deleteAll()
    }

    override fun findAll(): List<Student> = jpaStudentRepository.findAll()
}
