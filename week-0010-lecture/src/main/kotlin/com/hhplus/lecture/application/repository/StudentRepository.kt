package com.hhplus.lecture.application.repository

import com.hhplus.lecture.domain.entity.Student

interface StudentRepository {
    fun findById(studentId: Long): Student?

    fun deleteAll()
}
