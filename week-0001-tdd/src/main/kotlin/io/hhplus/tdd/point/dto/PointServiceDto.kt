package io.hhplus.tdd.point.dto

import io.hhplus.tdd.point.UserPoint

object PointServiceDto {
    data class Point(
        val id: Long,
        val point: Long,
        val updateMillis: Long,
    ) {
        companion object {
            fun of(userPointData: UserPoint): Point {
                return Point(
                    id = userPointData.id,
                    point = userPointData.point,
                    updateMillis = userPointData.updateMillis,
                )
            }
        }
    }
}
