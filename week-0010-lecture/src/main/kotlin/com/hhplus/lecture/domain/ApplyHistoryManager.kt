package com.hhplus.lecture.domain

import com.hhplus.lecture.infra.repository.ApplyHistoryRepository
import org.springframework.stereotype.Component

@Component
class ApplyHistoryManager(
    private val applyHistoryRepository: ApplyHistoryRepository,
)
