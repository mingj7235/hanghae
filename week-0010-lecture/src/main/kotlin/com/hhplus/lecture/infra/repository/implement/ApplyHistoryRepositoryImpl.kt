package com.hhplus.lecture.infra.repository.implement

import com.hhplus.lecture.infra.repository.ApplyHistoryRepository
import com.hhplus.lecture.infra.repository.jpa.JpaApplyHistoryRepository
import org.springframework.stereotype.Repository

@Repository
class ApplyHistoryRepositoryImpl(
    private val jpaApplyHistoryRepository: JpaApplyHistoryRepository,
) : ApplyHistoryRepository
