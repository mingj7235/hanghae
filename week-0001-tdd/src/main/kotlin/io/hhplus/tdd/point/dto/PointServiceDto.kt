package io.hhplus.tdd.point.dto

import io.hhplus.tdd.point.data.PointHistory
import io.hhplus.tdd.point.data.UserPoint
import io.hhplus.tdd.point.type.TransactionType

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

        fun charge(amount: Long): Point {
            val chargedPoint = this.point + amount
            return Point(
                id = this.id,
                point = chargedPoint,
                updateMillis = this.updateMillis,
            )
        }
    }

    data class History(
        val userId: Long,
        val details: List<Detail>,
    )

    data class Detail(
        val detailId: Long,
        val type: TransactionType,
        val amount: Long,
        val timeMillis: Long,
    ) {
        companion object {
            fun of(historyData: PointHistory): Detail {
                return Detail(
                    detailId = historyData.id,
                    type = historyData.type,
                    amount = historyData.amount,
                    timeMillis = historyData.timeMillis,
                )
            }
        }
    }
}
