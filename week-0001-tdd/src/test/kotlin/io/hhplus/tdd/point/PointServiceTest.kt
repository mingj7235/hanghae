package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryRepository
import io.hhplus.tdd.database.UserPointRepository
import io.hhplus.tdd.point.data.PointHistory
import io.hhplus.tdd.point.data.UserPoint
import io.hhplus.tdd.point.exception.PointException
import io.hhplus.tdd.point.type.TransactionType
import io.hhplus.tdd.user.exception.UserException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class PointServiceTest {
    @Mock
    private lateinit var pointHistoryRepository: PointHistoryRepository

    @Mock
    private lateinit var userManager: UserManager

    @Mock
    private lateinit var userPointRepository: UserPointRepository

    @InjectMocks
    private lateinit var pointService: PointService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Nested
    @DisplayName("[getPointBy] 회원 포인트 조회 서비스 테스트")
    inner class GetPointByTest {
        @Test
        @DisplayName("[Failure Case 1] 조회하려는 id 가 없는 회원의 포인트를 조회할 경우 예외를 던진다.")
        fun `getPointByNotExistUserId`() {
            val notExistUserId = 100L

            `when`(userManager.existUser(notExistUserId)).thenReturn(false)

            val exception =
                assertThrows<UserException.UserNotFound> {
                    pointService.getPointBy(userId = notExistUserId)
                }
            assertThat(exception)
                .message().contains("Not found user. [id] = [$notExistUserId]")
        }

        @Test
        @DisplayName("[Success case 1] 존재하는 id의 회원의 포인트를 조회할 경우 성공한다.")
        fun `getPointByUserIdSuccessTest`() {
            val existUserId = 0L
            val userPoint = UserPoint(0L, 100L, 100L)

            `when`(userManager.existUser(existUserId)).thenReturn(true)
            `when`(userPointRepository.selectById(existUserId)).thenReturn(userPoint)

            val point = pointService.getPointBy(existUserId)
            assertThat(point.id).isEqualTo(0L)
            assertThat(point.point).isEqualTo(100L)
            assertThat(point.updateMillis).isEqualTo(100L)
        }
    }

    @Nested
    @DisplayName("[getHistoryBy] 회원의 포인트 충전/이용 내역 조회 테스트")
    inner class GetHistoryByTest {
        @Test
        @DisplayName("[Failure Case 1] 조회하려는 id 가 없는 회원의 포인트를 조회할 경우 예외를 던진다.")
        fun `getHistoryByNotExistUserId`() {
            val notExistUserId = 100L

            `when`(userManager.existUser(notExistUserId)).thenReturn(false)

            val exception =
                assertThrows<UserException.UserNotFound> {
                    pointService.getHistoryBy(userId = notExistUserId)
                }
            assertThat(exception)
                .message().contains("Not found user. [id] = [$notExistUserId]")
        }

        @Test
        @DisplayName("[Success case 1] 존재하는 id의 회원의 포인트 내역을 조회할 경우 성공한다.")
        fun `getHistoryByUserIdSuccessTest`() {
            val existUserId = 0L
            val historyList =
                listOf(
                    PointHistory(0L, 0L, TransactionType.CHARGE, 1000L, 1000L),
                    PointHistory(1L, 0L, TransactionType.USE, 500L, 500L),
                )

            `when`(userManager.existUser(existUserId)).thenReturn(true)
            `when`(pointHistoryRepository.selectAllByUserId(existUserId)).thenReturn(historyList)

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
    }

    @Nested
    @DisplayName("[charge] 회원의 포인트 저장 테스트")
    inner class ChargeTest {
        @Test
        @DisplayName("[Failure Case 1] 포인트를 충전하려고 하는 회원의 id 가 없는 경우 실패한다.")
        fun `chargeToNotExistUserId`() {
            val notExistUserId = 100L
            val amount = 1000L

            `when`(userManager.existUser(notExistUserId)).thenReturn(false)

            val exception =
                assertThrows<UserException.UserNotFound> {
                    pointService.charge(id = notExistUserId, amount = amount)
                }
            assertThat(exception)
                .message().contains("Not found user. [id] = [$notExistUserId]")
        }

        @Test
        @DisplayName("[Success Case 1] 올바른 userId 와 amount 가 주어졌을 때 충전이 성공한다.")
        fun `chargeSuccessTest`() {
            val userId = 0L
            val amount = 1000L
            val userPoint = UserPoint(0L, 100L, 100L)
            val updatedUserPoint = UserPoint(0L, 1100L, 100L)

            `when`(userManager.existUser(userId)).thenReturn(true)
            `when`(userPointRepository.selectById(userId)).thenReturn(userPoint)
            `when`(userPointRepository.insertOrUpdate(userId, userPoint.point + amount)).thenReturn(updatedUserPoint)

            val chargedUserPoint = pointService.charge(id = userId, amount = amount)

            assertThat(chargedUserPoint.id).isEqualTo(0L)
            assertThat(chargedUserPoint.point).isEqualTo(1100L)
        }

        @Test
        @DisplayName("[Success Case 2] 올바른 userId 와 amount 가 주어졌을 때, 충전 내역이 저장된다.")
        fun `chargeSuccessHistorySaveTest`() {
            val userId = 0L
            val amount = 1000L
            val userPoint = UserPoint(0L, 100L, 100L)
            val updatedUserPoint = UserPoint(0L, 1100L, 100L)

            `when`(userManager.existUser(userId)).thenReturn(true)
            `when`(userPointRepository.selectById(userId)).thenReturn(userPoint)
            `when`(userPointRepository.insertOrUpdate(userId, userPoint.point + amount)).thenReturn(updatedUserPoint)

            val chargedUserPoint = pointService.charge(id = userId, amount = amount)

            verify(pointHistoryRepository, times(1)).insert(
                id = chargedUserPoint.id,
                amount = amount,
                transactionType = TransactionType.CHARGE,
                updateMillis = chargedUserPoint.updateMillis,
            )
        }
    }

    @Nested
    @DisplayName("[use] 회원의 포인트 사용 테스트")
    inner class UseTest {
        @Test
        @DisplayName("[Failure Case 1] 포인트를 사용하려고 하는 회원의 id 가 없는 경우 실패한다.")
        fun `useToNotExistUserId`() {
            val notExistUserId = 100L
            val amount = 1000L

            `when`(userManager.existUser(notExistUserId)).thenReturn(false)

            val exception =
                assertThrows<UserException.UserNotFound> {
                    pointService.use(id = notExistUserId, amount = amount)
                }
            assertThat(exception)
                .message().contains("Not found user. [id] = [$notExistUserId]")
        }

        @Test
        @DisplayName("[Failure Case 2] 포인트를 사용하려고 하는 회원의 기존 포인트가 사용하려는 포인트보다 적을 경우 실패한다.")
        fun `tryToOverPoint`() {
            val userId = 0L
            val amount = 1000L
            val userPoint = UserPoint(0L, 500L, 500L)

            `when`(userManager.existUser(userId)).thenReturn(true)
            `when`(userPointRepository.selectById(userId)).thenReturn(userPoint)

            val exception =
                assertThrows<PointException.InsufficientPointsException> {
                    pointService.use(id = userId, amount = amount)
                }
            assertThat(exception)
                .message().contains("Insufficient Point. Current Point : ${userPoint.point}")
        }

        @Test
        @DisplayName("[Success Case 1] 포인트 사용에 성공한다.")
        fun `usePointSuccssTest`() {
            val userId = 0L
            val amount = 1000L
            val userPoint = UserPoint(userId, 3000L, 3000L)
            val usedPoint = UserPoint(userId, 2000L, 20000L)

            `when`(userManager.existUser(userId)).thenReturn(true)
            `when`(userPointRepository.selectById(userId)).thenReturn(userPoint)
            `when`(
                userPointRepository.insertOrUpdate(
                    id = userId,
                    amount = usedPoint.point,
                ),
            ).thenReturn(usedPoint)

            val usedUserPoint = pointService.use(id = userId, amount = amount)

            assertThat(usedUserPoint.id).isEqualTo(usedPoint.id)
            assertThat(usedUserPoint.point).isEqualTo(usedPoint.point)
        }

        @Test
        @DisplayName("[Success Case 2] 포인트 사용에 성공하면, history 를 저장한다.")
        fun `usePointSuccssHistorySaveTest`() {
            val userId = 0L
            val amount = 1000L
            val userPoint = UserPoint(userId, 3000L, 3000L)
            val usedPoint = UserPoint(userId, 2000L, 20000L)

            `when`(userManager.existUser(userId)).thenReturn(true)
            `when`(userPointRepository.selectById(userId)).thenReturn(userPoint)
            `when`(
                userPointRepository.insertOrUpdate(
                    id = userId,
                    amount = usedPoint.point,
                ),
            ).thenReturn(usedPoint)

            val usedUserPoint = pointService.use(id = userId, amount = amount)

            verify(pointHistoryRepository, times(1)).insert(
                id = usedUserPoint.id,
                amount = amount,
                transactionType = TransactionType.USE,
                updateMillis = usedUserPoint.updateMillis,
            )
        }
    }
}
