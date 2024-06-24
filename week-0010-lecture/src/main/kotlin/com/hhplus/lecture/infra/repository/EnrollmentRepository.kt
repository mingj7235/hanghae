package com.hhplus.lecture.infra.repository

import com.hhplus.lecture.infra.entity.Enrollment
import org.springframework.data.jpa.repository.JpaRepository

interface EnrollmentRepository : JpaRepository<Enrollment, Long>
