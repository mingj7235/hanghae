package io.hhplus.tdd.point

import io.hhplus.tdd.database.PointHistoryRepository
import io.hhplus.tdd.database.UserPointRepository
import io.hhplus.tdd.database.UserRepository
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
        @DisplayName("[Success case] 존재하는 id의 회원의 포인트를 조회할 경우 성공한다.")
        fun `getPointByUserIdSuccessTest`() {
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
}
