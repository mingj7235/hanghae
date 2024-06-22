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
        val userId: Long,
        val details: List<Detail>,
    ) {
        companion object {
            fun of(historyDto: PointServiceDto.History): History {
                return History(
                    userId = historyDto.userId,
                    details =
                        historyDto.details.map {
                            Detail.of(it)
                        },
                )
            }
        }
    }

    data class Detail(
        val detailId: Long,
        val type: TransactionType,
        val amount: Long,
        val timeMillis: Long,
    ) {
        companion object {
            fun of(detailDto: PointServiceDto.Detail): Detail {
                return Detail(
                    detailId = detailDto.detailId,
                    type = detailDto.type,
                    amount = detailDto.amount,
                    timeMillis = detailDto.timeMillis,
                )
            }
        }
    }
}
