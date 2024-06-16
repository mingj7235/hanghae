package io.hhplus.tdd.database

import io.hhplus.tdd.point.data.PointHistory
import io.hhplus.tdd.point.type.TransactionType

interface PointHistoryRepository {
    fun insert(
        id: Long,
        amount: Long,
        transactionType: TransactionType,
        updateMillis: Long,
    ): PointHistory

    fun selectAllByUserId(
        userId: Long
    ): List<PointHistory>
}