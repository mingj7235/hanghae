package io.hhplus.tdd.point

import com.fasterxml.jackson.databind.ObjectMapper
import io.hhplus.tdd.database.PointHistoryRepository
import io.hhplus.tdd.database.UserPointRepository
import io.hhplus.tdd.database.UserRepository
import io.hhplus.tdd.point.type.TransactionType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import java.lang.reflect.Field
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
@AutoConfigureMockMvc
class PointIntegrationTest(
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val pointService: PointService,
    @Autowired private val userManager: UserManager,
    @Autowired private val pointHistoryRepository: PointHistoryRepository,
    @Autowired private val userPointRepository: UserPointRepository,
    @Autowired private val userRepository: UserRepository,
) {
    @AfterEach
    fun dataInit() {
        clearTable(userRepository)
        clearTable(pointHistoryRepository)
        clearTable(userPointRepository)
        resetCursor(pointHistoryRepository)
    }

    private fun clearTable(repository: Any) {
        val tableField: Field = repository::class.java.getDeclaredField("table")
        tableField.isAccessible = true
        when (val table = tableField.get(repository)) {
            is MutableMap<*, *> -> table.clear()
            is MutableList<*> -> table.clear()
        }
    }

    private fun resetCursor(repository: Any) {
        val cursorField: Field = repository::class.java.getDeclaredField("cursor")
        cursorField.isAccessible = true
        cursorField.set(repository, 1L)
    }

    @Nested
    @DisplayName("[point] 유저의 포인트 조회 API 통합 테스트")
    inner class PointApiTest {
        @Test
        fun `존재하지 않은 유저의 포인트를 조회하면 예외를 리턴한다`() {
            mockMvc.get("/point/$NON_EXISTED_USER_ID")
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.message") { value("Not found user. [id] = [$NON_EXISTED_USER_ID]") }
                }
        }

        @Test
        fun `존재하는 id의 유저의 포인트를 조회할 경우 성공한다`() {
            // Given
            val existUserId = 0L
            val point = 1000L

            val user = userRepository.save(existUserId)
            val userPoint = userPointRepository.insertOrUpdate(user.id, point)

            // When
            val result =
                mockMvc.get("/point/$existUserId") {
                    contentType = MediaType.APPLICATION_JSON
                }

            // Then
            result.andExpect {
                status { isOk() }
                jsonPath("$.id") { value(user.id) }
                jsonPath("$.point") { value(userPoint.point) }
                jsonPath("$.updateMillis") { value(userPoint.updateMillis) }
            }
        }
    }

    @Nested
    @DisplayName("[history] 유저의 포인트 충전/이용 내역 조회 API 통합 테스트")
    inner class HistoryApiTest {
        @Test
        fun `존재하지 않은 유저의 포인트를 조회하면 예외를 리턴한다`() {
            mockMvc.get("/point/$NON_EXISTED_USER_ID/histories")
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.message") { value("Not found user. [id] = [$NON_EXISTED_USER_ID]") }
                }
        }

        @Test
        fun `한 유저가 충전을 1회를 했었다면, 충전 내역 조회가 성공한다`() {
            // Given
            val existUserId = 0L
            val chargingPoint = 10000L

            val user = userRepository.save(existUserId)
            val chargedUserPoint = pointService.charge(existUserId, chargingPoint)

            val result =
                mockMvc.get("/point/$existUserId/histories") {
                    contentType = MediaType.APPLICATION_JSON
                }

            result.andExpectAll {
                status { isOk() }
                jsonPath("$.userId") { value(user.id) }
                jsonPath("$.details[0].detailId") { value(1L) }
                jsonPath("$.details[0].type") { value(TransactionType.CHARGE.toString()) }
                jsonPath("$.details[0].amount") { value(chargedUserPoint.point) }
                jsonPath("$.details[0].timeMillis") { value(chargedUserPoint.updateMillis) }
            }
        }

        @Test
        fun `한 유저가 충전을 1회, 사용 1회를 했었다면, 충전 1회 사용 1회 내역 조회가 성공한다`() {
            // Given
            val existUserId = 0L
            val chargingPoint = 10000L
            val usingPoint = 5000L

            val user = userRepository.save(existUserId)
            val chargedUserPoint = pointService.charge(existUserId, chargingPoint)
            val usedUserPoint = pointService.use(existUserId, usingPoint)

            val result =
                mockMvc.get("/point/$existUserId/histories") {
                    contentType = MediaType.APPLICATION_JSON
                }

            result.andExpectAll {
                status { isOk() }
                jsonPath("$.userId") { value(user.id) }
                jsonPath("$.details[0].detailId") { value(1L) }
                jsonPath("$.details[0].type") { value(TransactionType.CHARGE.toString()) }
                jsonPath("$.details[0].amount") { value(chargedUserPoint.point) }
                jsonPath("$.details[0].timeMillis") { value(chargedUserPoint.updateMillis) }
                jsonPath("$.details[1].detailId") { value(2L) }
                jsonPath("$.details[1].type") { value(TransactionType.USE.toString()) }
                jsonPath("$.details[1].amount") { value(usedUserPoint.point) }
                jsonPath("$.details[1].timeMillis") { value(usedUserPoint.updateMillis) }
            }
        }
    }

    @Nested
    @DisplayName("[charge] 유저의 포인트 충전 API 통합 테스트")
    inner class ChargeApiTest {
        @Test
        fun `존재하지 않은 유저의 포인트를 충전하려고 하면 예외를 리턴한다`() {
            val amount = 1000L
            mockMvc.patch("/point/$NON_EXISTED_USER_ID/charge") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(amount)
            }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.message") { value("Not found user. [id] = [$NON_EXISTED_USER_ID]") }
                }
        }

        @Test
        fun `음수의 포인트를 충전하려고 하면 예외를 리턴한다`() {
            val existUserId = 0L
            val amount = -1000L

            userRepository.save(existUserId)

            mockMvc.patch("/point/$existUserId/charge") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(amount)
            }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.message") { value("Invalid Charge Point : [$amount]") }
                }
        }

        @Test
        fun `포인트 충전이 정상적으로 성공한다`() {
            val existUserId = 0L
            val amount = 10000L

            val user = userRepository.save(existUserId)

            mockMvc.patch("/point/$existUserId/charge") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(amount)
            }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.id") { value(user.id) }
                    jsonPath("$.point") { value(amount) }
                }
        }

        @Test
        fun `기존 포인트가 존재하던 유저의 포인트를 충전하면 기존 금액에 정상적으로 더해진다`() {
            val existUserId = 0L
            val currentPoint = 10000L
            val amount = 5000L

            val user = userRepository.save(existUserId)
            userPointRepository.insertOrUpdate(id = 0L, amount = currentPoint)

            mockMvc.patch("/point/$existUserId/charge") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(amount)
            }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.id") { value(user.id) }
                    jsonPath("$.point") { value(currentPoint + amount) }
                }
        }
    }

    @Nested
    @DisplayName("[use] 유저의 포인트 사용 API 통합 테스트")
    inner class UseApiTest {
        @Test
        fun `존재하지 않은 유저의 포인트를 충전하려고 하면 예외를 리턴한다`() {
            val amount = 1000L
            mockMvc.patch("/point/$NON_EXISTED_USER_ID/use") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(amount)
            }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.message") { value("Not found user. [id] = [$NON_EXISTED_USER_ID]") }
                }
        }

        @Test
        fun `현재 유저가 가지고 있는 포인트보다 더 많은 포인트를 사용하려고 하면 예외를 리턴한다`() {
            val existUserId = 0L
            val currentPoint = 500L
            val amount = 1000L

            val user = userRepository.save(existUserId)
            userPointRepository.insertOrUpdate(id = user.id, amount = currentPoint)

            mockMvc.patch("/point/$existUserId/use") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(amount)
            }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.message") { value("Insufficient Point. Current Point : $currentPoint") }
                }
        }

        @Test
        fun `현재 유저가 사용하려는 포인트보다 더 많은 포인트가 있다면 정상적으로 포인트가 사용된다`() {
            val existUserId = 0L
            val currentPoint = 5000L
            val amount = 1000L

            val user = userRepository.save(existUserId)
            userPointRepository.insertOrUpdate(id = user.id, amount = currentPoint)

            mockMvc.patch("/point/$existUserId/use") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(amount)
            }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.id") { value(user.id) }
                    jsonPath("$.point") { value(currentPoint - amount) }
                }
        }
    }

    @Nested
    @DisplayName("[동시성] 회원의 포인트 충전 및 사용 동시성 테스트")
    inner class PointConcurrencyTest {
        @Test
        fun `포인트를 동시에 여러번 충전을 하면 순차적으로 충전이 된다`() {
            // given
            val userId = 0L
            val amount = 10L
            val numberOfThreads = 100
            userRepository.save(userId)

            val executor = Executors.newFixedThreadPool(numberOfThreads)
            val latch = CountDownLatch(numberOfThreads)

            // when
            for (i in 0 until numberOfThreads) {
                executor.submit {
                    try {
                        pointService.charge(userId, amount)
                    } finally {
                        latch.countDown()
                    }
                }
            }
            latch.await()
            executor.shutdown()

            // then
            val userPoint = pointService.getPointBy(userId)
            assertEquals(amount * numberOfThreads, userPoint.point)
        }
    }

    companion object {
        const val NON_EXISTED_USER_ID = -1L
    }
}
