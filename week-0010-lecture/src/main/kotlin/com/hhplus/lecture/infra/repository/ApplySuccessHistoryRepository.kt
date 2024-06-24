package com.hhplus.lecture.infra.repository

import com.hhplus.lecture.infra.entity.ApplySuccessHistory
import org.springframework.data.jpa.repository.JpaRepository

interface ApplySuccessHistoryRepository : JpaRepository<ApplySuccessHistory, Long>
