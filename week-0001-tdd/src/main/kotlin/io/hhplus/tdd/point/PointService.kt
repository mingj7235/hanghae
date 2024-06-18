package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryRepository
import io.hhplus.tdd.database.UserPointRepository
import io.hhplus.tdd.point.dto.PointServiceDto
import io.hhplus.tdd.point.exception.PointException
import io.hhplus.tdd.point.type.TransactionType
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

    fun charge(
        id: Long,
        amount: Long,
    ): PointServiceDto.Point {
        if (!userManager.existUser(id)) {
            throw UserException.UserNotFound("Not found user. [id] = [$id]")
        }

        val currentUserPoint = userPointRepository.selectById(id)

        val chargedUserPoint =
            PointServiceDto.Point.of(
                userPointRepository.insertOrUpdate(
                    id = currentUserPoint.id,
                    amount = currentUserPoint.point + amount,
                ),
            )

        pointHistoryRepository.insert(
            id = id,
            amount = amount,
            transactionType = TransactionType.CHARGE,
            updateMillis = chargedUserPoint.updateMillis,
        )

        return chargedUserPoint
    }

    fun use(
        id: Long,
        amount: Long,
    ): PointServiceDto.Point {
        if (!userManager.existUser(id)) {
            throw UserException.UserNotFound("Not found user. [id] = [$id]")
        }

        val currentUserPoint = userPointRepository.selectById(id)

        if (amount > currentUserPoint.point) {
            throw PointException.InsufficientPointsException("Insufficient Point. Current Point : ${currentUserPoint.point}")
        }

        TODO()
    }
}
