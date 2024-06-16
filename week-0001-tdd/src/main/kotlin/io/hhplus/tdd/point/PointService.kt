package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryTable
import io.hhplus.tdd.database.UserPointTable
import io.hhplus.tdd.point.dto.PointServiceDto
import org.springframework.stereotype.Service

@Service
class PointService(
    private val pointHistoryTable: PointHistoryTable,
    private val userPointTable: UserPointTable,
) {
    fun getPointBy(userId: Long): PointServiceDto.Point {
        return PointServiceDto.Point.of(userPointTable.selectById(userId))
    }
}
