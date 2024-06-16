package io.hhplus.tdd.point

import io.hhplus.tdd.point.dto.PointServiceDto
import io.hhplus.tdd.point.type.TransactionType
import io.hhplus.tdd.user.exception.UserException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch

@WebMvcTest(PointController::class)
class PointControllerTest(
    @Autowired val mockMvc: MockMvc,
) {
    @MockBean
    lateinit var pointService: PointService

    @Test
    @DisplayName("올바르지 않은 형태의 id가 들어올 경우 Bad Request 를 리턴한다.")
    fun `getPointByInvalidTypePathVariable`() {
        // Given
        val wrongValue = "wrongValue"

        // When
        val result = mockMvc.get("/point/$wrongValue")

        // Then
        result.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    @DisplayName("존재하지 않은 id 의 회원으로 포인트를 조회할 경우 예외를 리턴한다.")
    fun `getPointByNotExistUserId`() {
        // Given
        val notExistId = -1L
        given(
            pointService.getPointBy(notExistId),
        ).willThrow(
            UserException.UserNotFound("Not found user. [id] = [$notExistId]"),
        )

        // When
        val result = mockMvc.get("/point/$notExistId")

        // Then
        result.andExpectAll {
            status { isInternalServerError() }
            jsonPath("$.message") { value("에러가 발생했습니다.") }
        }
    }

    @Test
    @DisplayName("정상적으로 존재하는 id 의 회원의 포인트를 조회할 경우 그 회원의 포인트를 리턴한다.")
    fun `getUserPointApiTest`() {
        // Given
        val userId = 0L
        val userPoint = 100L
        val updateMillis = 100L

        given(
            pointService.getPointBy(userId),
        ).willReturn(
            PointServiceDto.Point(
                id = userId,
                point = userPoint,
                updateMillis = updateMillis,
            ),
        )

        // When
        val result = mockMvc.get("/point/$userId")

        // Then
        result.andExpectAll {
            status { isOk() }
            jsonPath("$.id") { value(userId) }
            jsonPath("$.point") { value(userPoint) }
            jsonPath("$.updateMillis") { value(updateMillis) }
        }
    }

    @Test
    @DisplayName("올바르지 않은 형태의 id가 들어올 경우 Bad Request 를 리턴한다.")
    fun `getHistoryByInvalidTypePathVariable`() {
        // Given
        val wrongValue = "wrongValue"

        // When
        val result = mockMvc.get("/point/$wrongValue/histories")

        // Then
        result.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    @DisplayName("존재하지 않은 id 의 회원으로 포인트를 조회할 경우 예외를 리턴한다.")
    fun `getHistoryByNotExistUserId`() {
        // Given
        val notExistId = -1L
        given(
            pointService.getHistoryBy(notExistId),
        ).willThrow(
            UserException.UserNotFound("Not found user. [id] = [$notExistId]"),
        )

        // When
        val result = mockMvc.get("/point/$notExistId/histories")

        // Then
        result.andExpectAll {
            status { isInternalServerError() }
            jsonPath("$.message") { value("에러가 발생했습니다.") }
        }
    }

    @Test
    @DisplayName("특정 유저의 포인트 충전 및 이용 내역을 조회한다.")
    fun `getUserPointHistoryApiTest`() {
        // Given
        val userId = 0L
        val detailId = 0L
        val type = TransactionType.USE
        val amount = 1000L
        val timeMillis = 1000L

        given(
            pointService.getHistoryBy(userId),
        ).willReturn(
            PointServiceDto.History(
                userId = userId,
                details =
                    listOf(
                        PointServiceDto.Detail(
                            detailId = detailId,
                            type = type,
                            amount = amount,
                            timeMillis = timeMillis,
                        ),
                    ),
            ),
        )

        // When
        val result = mockMvc.get("/point/$userId/histories")

        // Then
        result.andExpect {
            jsonPath("$.userId") { value(0L) }
            jsonPath("$.details[0].detailId") { value(detailId) }
            jsonPath("$.details[0].type") { value(type.toString()) }
            jsonPath("$.details[0].amount") { value(amount) }
            jsonPath("$.details[0].timeMillis") { value(timeMillis) }
        }
    }

    @Test
    @DisplayName("특정 유저의 포인트를 충전한다.")
    fun `patchChargePointApiTest`() {
        val amount = 100L
        val result =
            mockMvc.patch("/point/0/charge") {
                contentType = APPLICATION_JSON
                content = "\"\"\"\n" +
                    "                    {\n" +
                    "                        \"amount\": $amount\n" +
                    "                    }\n" +
                    "                \"\"\""
            }
        result.andExpect {
            status { isOk() }
            jsonPath("$.id") { value(0) }
            jsonPath("$.point") { value(0) }
            jsonPath("$.updateMillis") { value(0) }
        }
    }

    @Test
    @DisplayName("특정 유저의 포인트를 사용한다.")
    fun `patchUsePointApiTest`() {
        val amount = 100L
        val result =
            mockMvc.patch("/point/0/use") {
                contentType = APPLICATION_JSON
                content = "\"\"\"\n" +
                    "                    {\n" +
                    "                        \"amount\": $amount\n" +
                    "                    }\n" +
                    "                \"\"\""
            }
        result.andExpect {
            status { isOk() }
            jsonPath("$.id") { value(0) }
            jsonPath("$.point") { value(0) }
            jsonPath("$.updateMillis") { value(0) }
        }
    }
}
