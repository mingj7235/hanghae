package com.hhplus.lecture.infra.repository.jpa

import com.hhplus.lecture.infra.entity.Student
import org.springframework.data.jpa.repository.JpaRepository

interface JpaStudentRepository : JpaRepository<Student, Long>
