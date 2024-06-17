package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryRepository
import io.hhplus.tdd.database.UserPointRepository
import io.hhplus.tdd.database.UserRepository
import io.hhplus.tdd.point.data.PointHistory
import io.hhplus.tdd.point.data.UserPoint
import io.hhplus.tdd.point.type.TransactionType
import io.hhplus.tdd.user.data.User
import io.hhplus.tdd.user.exception.UserException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PointServiceTest {
    private val pointService =
        PointService(
            userManager = UserManagerStub(),
            pointHistoryRepository = PointHistoryRepositoryStub(),
            userPointRepository = UserPointRepositoryStub(),
        )

    @Test
    @DisplayName("Failure Case 1: 조회하려는 id 가 없는 회원의 포인트를 조회할 경우 예외를 던진다.")
    fun `getPointByNotExistUserId`() {
        val notExistUserId = 100L

        val exception =
            assertThrows<UserException.UserNotFound> {
                pointService.getPointBy(userId = notExistUserId)
            }
        assertThat(exception)
            .message().contains("Not found user. [id] = [$notExistUserId]")
    }

    @Test
    @DisplayName("Success case 1 : 존재하는 id의 회원의 포인트를 조회할 경우 성공한다.")
    fun `getPointByUserIdSuccessTest`() {
        val existUserId = 0L

        val point = pointService.getPointBy(existUserId)
        assertThat(point.id).isEqualTo(0L)
        assertThat(point.point).isEqualTo(100L)
        assertThat(point.updateMillis).isEqualTo(100L)
    }

    // /histories
    // Failure Case 1 : 조회하려는 id 가 없는 회원의 내역을 조회할 경우 예외를 던진다.
    @Test
    @DisplayName("Failure Case 1: 조회하려는 id 가 없는 회원의 포인트를 조회할 경우 예외를 던진다.")
    fun `getHistoryByNotExistUserId`() {
        val notExistUserId = 100L

        val exception =
            assertThrows<UserException.UserNotFound> {
                pointService.getHistoryBy(userId = notExistUserId)
            }
        assertThat(exception)
            .message().contains("Not found user. [id] = [$notExistUserId]")
    }

    // Success Case 1: 존재하는 id 의 회원의 포인트를 조회할 경우 성공한다.
    @Test
    @DisplayName("Success case 1 : 존재하는 id의 회원의 포인트를 조회할 경우 성공한다.")
    fun `getHistoryByUserIdSuccessTest`() {
        val existUserId = 0L

        val history = pointService.getHistoryBy(existUserId)
        assertThat(history.userId).isEqualTo(0L)
        assertThat(history.details[0].detailId).isEqualTo(0L)
        assertThat(history.details[0].type).isEqualTo(TransactionType.CHARGE)
        assertThat(history.details[0].amount).isEqualTo(1000L)
        assertThat(history.details[0].timeMillis).isEqualTo(1000L)
        assertThat(history.details[1].detailId).isEqualTo(1L)
        assertThat(history.details[1].type).isEqualTo(TransactionType.USE)
        assertThat(history.details[1].amount).isEqualTo(500L)
        assertThat(history.details[1].timeMillis).isEqualTo(500L)
    }

    class UserManagerStub : UserManager(UserRepositorySub()) {
        override fun existUser(userId: Long): Boolean {
            return when (userId) {
                0L, 1L, 2L -> true
                else -> false
            }
        }
    }

    class UserRepositorySub : UserRepository {
        override fun findBy(id: Long): User? {
            return when (id) {
                0L -> User(0L)
                1L -> User(1L)
                2L -> User(2L)
                else -> null
            }
        }
    }

    class PointHistoryRepositoryStub : PointHistoryRepository {
        override fun insert(
            id: Long,
            amount: Long,
            transactionType: TransactionType,
            updateMillis: Long,
        ): PointHistory {
            TODO("Not yet implemented")
        }

        override fun selectAllByUserId(userId: Long): List<PointHistory> {
            return when (userId) {
                0L ->
                    listOf(
                        PointHistory(
                            id = 0L,
                            userId = 0L,
                            type = TransactionType.CHARGE,
                            amount = 1000L,
                            timeMillis = 1000L,
                        ),
                        PointHistory(
                            id = 1L,
                            userId = 0L,
                            type = TransactionType.USE,
                            amount = 500L,
                            timeMillis = 500L,
                        ),
                    )
                else -> emptyList()
            }
        }
    }

    class UserPointRepositoryStub : UserPointRepository {
        override fun selectById(id: Long): UserPoint {
            return when (id) {
                0L -> UserPoint(0L, 100L, 100L)
                1L -> UserPoint(1L, 200L, 200L)
                2L -> UserPoint(2L, 300L, 300L)
                else -> UserPoint(-1L, -100L, -100L)
            }
        }

        override fun insertOrUpdate(
            id: Long,
            amount: Long,
        ): UserPoint {
            TODO("Not yet implemented")
        }
    }
}
