package com.hhplus.lecture.infra.repository

import com.hhplus.lecture.infra.entity.ApplyHistory
import org.springframework.data.jpa.repository.JpaRepository

interface ApplyHistoryRepository : JpaRepository<ApplyHistory, Long>
