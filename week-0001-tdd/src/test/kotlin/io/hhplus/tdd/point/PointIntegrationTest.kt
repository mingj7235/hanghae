package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryRepository
import io.hhplus.tdd.database.UserPointRepository
import io.hhplus.tdd.database.UserRepository
import io.hhplus.tdd.point.type.TransactionType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class PointIntegrationTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var pointService: PointService

    @Autowired
    private lateinit var userManager: UserManager

    @Autowired
    private lateinit var pointHistoryRepository: PointHistoryRepository

    @Autowired
    private lateinit var userPointRepository: UserPointRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Nested
    @DisplayName("[point] 회원의 포인트 조회 API 통합 테스트")
    inner class PointApiTest {
        @Test
        fun `존재하지 않은 회원의 포인트를 조회하면 예외를 리턴한다`() {
            mockMvc.get("/point/$NON_EXISTED_USER_ID")
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.message") { value("Not found user. [id] = [$NON_EXISTED_USER_ID]") }
                }
        }

        @Test
        fun `존재하는 id의 회원의 포인트를 조회할 경우 성공한다`() {
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
    @DisplayName("[history] 회원의 포인트 충전/이용 내역 조회 API 테스트")
    inner class HistoryApiTest {
        @Test
        fun `존재하지 않은 회원의 포인트를 조회하면 예외를 리턴한다`() {
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

    companion object {
        val NON_EXISTED_USER_ID = -1L
    }
}
