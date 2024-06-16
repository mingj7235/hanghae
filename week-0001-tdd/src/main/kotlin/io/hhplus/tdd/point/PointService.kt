package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryRepository
import io.hhplus.tdd.database.UserPointRepository
import io.hhplus.tdd.point.dto.PointServiceDto
import io.hhplus.tdd.user.exception.UserException
import org.springframework.stereotype.Service

@Service
class PointService(
    private val userManager: UserManager,
    private val pointHistoryRepository: PointHistoryRepository,
    private val userPointRepository: UserPointRepository,
) {
    fun getPointBy(userId: Long): PointServiceDto.Point {
        if (!userManager.existUser(userId)) {
            throw UserException.UserNotFound("Not found user. [id] = [$userId]")
        }
        return PointServiceDto.Point.of(userPointRepository.selectById(userId))
    }

    fun getHistoryBy(userId: Long): PointServiceDto.History {
        if (!userManager.existUser(userId)) {
            throw UserException.UserNotFound("Not found user. [id] = [$userId]")
        }

        val details =
            pointHistoryRepository.selectAllByUserId(userId)
                .map { detail -> PointServiceDto.Detail.of(detail) }

        return PointServiceDto.History(
            userId = userId,
            details = details,
        )
    }
}
