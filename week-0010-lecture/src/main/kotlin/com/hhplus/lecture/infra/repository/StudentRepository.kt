package com.hhplus.lecture.infra.repository

import com.hhplus.lecture.infra.entity.Student
import org.springframework.data.jpa.repository.JpaRepository

interface StudentRepository : JpaRepository<Student, Long>
