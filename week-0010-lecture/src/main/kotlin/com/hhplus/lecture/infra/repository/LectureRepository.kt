package com.hhplus.lecture.infra.repository

import com.hhplus.lecture.infra.entity.Lecture
import org.springframework.data.jpa.repository.JpaRepository

interface LectureRepository : JpaRepository<Lecture, Long>
