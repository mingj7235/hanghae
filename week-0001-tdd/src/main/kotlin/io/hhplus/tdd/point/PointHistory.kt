package io.hhplus.tdd.point

import io.hhplus.tdd.point.type.TransactionType

data class PointHistory(
    val id: Long,
    val userId: Long,
    val type: TransactionType,
    val amount: Long,
    val timeMillis: Long,
)
