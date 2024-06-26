package com.hhplus.lecture.infra.repository.jpa

import com.hhplus.lecture.domain.entity.Lecture
import org.springframework.data.jpa.repository.JpaRepository

interface JpaLectureRepository : JpaRepository<Lecture, Long>
