package io.hhplus.tdd.point.response

import io.hhplus.tdd.point.dto.PointServiceDto
import io.hhplus.tdd.point.type.TransactionType

object PointResponse {
    data class Point(
        val id: Long,
        val point: Long,
        val updateMillis: Long,
    ) {
        companion object {
            fun of(pointDto: PointServiceDto.Point): Point {
                return Point(
                    id = pointDto.id,
                    point = pointDto.point,
                    updateMillis = pointDto.updateMillis,
                )
            }
        }
    }

    data class History(
        val id: Long,
        val userId: Long,
        val type: TransactionType,
        val amount: Long,
        val timeMillis: Long,
    )
}
