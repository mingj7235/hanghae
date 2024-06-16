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
    private val pointService = PointService(
        userManager = UserManagerStub(),
        pointHistoryRepository = PointHistoryRepositoryStub(),
        userPointRepository = UserPointRepositoryStub()
    )

    @Test
    @DisplayName("Failure Case 1: 조회하려는 id 가 없는 회원의 포인트를 조회할 경우 예외를 던진다.")
    fun `getPointByNotExistUserId`() {
        val notExistUserId = 100L

        val exception = assertThrows<UserException.UserNotFound> {
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

    class UserManagerStub : UserManager(UserRepositorySub()) {
        override fun existUser(userId: Long): Boolean {
            return when(userId) {
                0L, 1L, 2L -> true
                else -> false
            }
        }
    }

    class UserRepositorySub : UserRepository {
        override fun findBy(id: Long): User? {
            return when(id) {
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
            updateMillis: Long
        ): PointHistory {
            TODO("Not yet implemented")
        }

        override fun selectAllByUserId(userId: Long): List<PointHistory> {
            TODO("Not yet implemented")
        }
    }

    class UserPointRepositoryStub : UserPointRepository  {
        override fun selectById(id: Long): UserPoint {
            return when(id) {
                0L -> UserPoint(0L, 100L, 100L)
                1L -> UserPoint(1L, 200L, 200L)
                2L -> UserPoint(2L, 300L, 300L)
                else -> UserPoint(-1L, -100L, -100L)
            }
        }

        override fun insertOrUpdate(id: Long, amount: Long): UserPoint {
            TODO("Not yet implemented")
        }
    }
}
