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
}
