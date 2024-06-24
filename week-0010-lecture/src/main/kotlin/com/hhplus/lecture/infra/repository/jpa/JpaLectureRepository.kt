package com.hhplus.lecture.infra.repository.jpa

import com.hhplus.lecture.infra.entity.Lecture
import org.springframework.data.jpa.repository.JpaRepository

interface JpaLectureRepository : JpaRepository<Lecture, Long>
