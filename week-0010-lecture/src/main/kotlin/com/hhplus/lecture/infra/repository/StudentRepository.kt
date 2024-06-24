package com.hhplus.lecture.infra.repository

import com.hhplus.lecture.infra.entity.Student

interface StudentRepository {
    fun findById(studentId: Long): Student?

    fun deleteAll()
}
