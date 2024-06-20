package io.hhplus.tdd.point

import com.fasterxml.jackson.databind.ObjectMapper
import io.hhplus.tdd.point.dto.PointServiceDto
import io.hhplus.tdd.point.exception.PointException
import io.hhplus.tdd.point.type.TransactionType
import io.hhplus.tdd.user.exception.UserException
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch

@WebMvcTest(PointController::class)
class PointControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper,
) {
    @MockBean
    lateinit var pointService: PointService

    @Nested
    @DisplayName("[point] 유저의 포인트 조회 API 테스트")
    inner class PointApiTest {
        @Test
        fun `올바르지 않은 형태의 id가 들어올 경우 Bad Request 를 리턴한다`() {
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
        fun `존재하지 않은 id 의 유저로 포인트를 조회할 경우 예외를 리턴한다`() {
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
                status { isBadRequest() }
                jsonPath("$.message") { value("Not found user. [id] = [$notExistId]") }
            }
        }

        @Test
        fun `정상적으로 존재하는 id 의 유저의 포인트를 조회할 경우 그 유저의 포인트를 리턴한다`() {
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
    }

    @Nested
    @DisplayName("[history] 유저의 포인트 충전/이용 내역 조회 API 테스트")
    inner class HistoryApiTest {
        @Test
        fun `올바르지 않은 형태의 id가 들어올 경우 Bad Request 를 리턴한다`() {
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
        fun `존재하지 않은 id 의 유저로 포인트를 조회할 경우 예외를 리턴한다`() {
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
                status { isBadRequest() }
                jsonPath("$.message") { value("Not found user. [id] = [$notExistId]") }
            }
        }

        @Test
        fun `특정 유저의 포인트 충전 및 이용 내역을 조회한다`() {
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
    }

    @Nested
    @DisplayName("[charge] 유저의 포인트 충전 API 테스트")
    inner class ChargeApiTest {
        @Test
        fun `올바르지 않은 형태의 id가 들어올 경우 Bad Request 를 리턴한다`() {
            // Given
            val wrongValue = "wrongValue"

            // When
            val result = mockMvc.patch("/point/$wrongValue/charge")

            // Then
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `RequestBody 가 없는 경우 Bad Request 를 리턴한다`() {
            // Given
            val userId = 1

            // When
            val result = mockMvc.patch("/point/$userId/charge")

            // Then
            result.andExpect {
                status { isBadRequest() }
            }
        }

        @Test
        fun `존재하지 않은 id 의 유저로 포인트를 조회할 경우 예외를 리턴한다`() {
            // Given
            val notExistId = -1L
            val amount = 1000L

            given(
                pointService.charge(
                    id = notExistId,
                    amount = 1000,
                ),
            ).willThrow(
                UserException.UserNotFound("Not found user. [id] = [$notExistId]"),
            )

            // When
            val result =
                mockMvc.patch("/point/$notExistId/charge") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(amount)
                }

            // Then
            result.andExpect {
                status { isBadRequest() }
                jsonPath("$.message") { value("Not found user. [id] = [$notExistId]") }
            }
        }

        @Test
        fun `음수 값은 충전이 불가능하다`() {
            // Given
            val userId = 0L
            val amount = -1000L

            given(
                pointService.charge(
                    id = userId,
                    amount = -1000,
                ),
            ).willThrow(
                PointException.InvalidAmountException("Invalid Charge Point : [$amount]"),
            )

            // When
            val result =
                mockMvc.patch("/point/$userId/charge") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(amount)
                }

            // Then
            result.andExpect {
                status { isBadRequest() }
                jsonPath("$.message") { value("Invalid Charge Point : [$amount]") }
            }
        }
        //

        @Test
        fun `특정 유저의 포인트를 충전한다`() {
            // Given
            val userId = 0L
            val amount = 100L
            val userPoint =
                PointServiceDto.Point(
                    id = 0L,
                    point = 100L,
                    updateMillis = 100L,
                )

            given(
                pointService.charge(
                    id = userId,
                    amount = amount,
                ),
            ).willReturn(userPoint)

            // When
            val result =
                mockMvc.patch("/point/$userId/charge") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(amount)
                }

            // Then
            result.andExpect {
                status { isOk() }
                jsonPath("$.id") { value(0) }
                jsonPath("$.point") { value(100L) }
                jsonPath("$.updateMillis") { value(100L) }
            }
        }
    }

    @Nested
    @DisplayName("[use] 유저의 포인트 사용 API 테스트")
    inner class UseApiTest {
        @Test
        fun `존재하지 않은 id 의 유저로 포인트를 조회할 경우 예외를 리턴한다`() {
            // Given
            val notExistId = -1L
            val amount = 1000L

            given(
                pointService.use(
                    id = notExistId,
                    amount = 1000,
                ),
            ).willThrow(
                UserException.UserNotFound("Not found user. [id] = [$notExistId]"),
            )

            // When
            val result =
                mockMvc.patch("/point/$notExistId/use") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(amount)
                }

            // Then
            result.andExpect {
                status { isBadRequest() }
                jsonPath("$.message") { value("Not found user. [id] = [$notExistId]") }
            }
        }

        @Test
        fun `특정 유저의 포인트를 사용한다`() {
            // Given
            val userId = 0L
            val amount = 1000L
            val userPoint =
                PointServiceDto.Point(
                    id = 0L,
                    point = 2000L,
                    updateMillis = 100L,
                )

            given(
                pointService.use(
                    id = userId,
                    amount = amount,
                ),
            ).willReturn(userPoint)

            // When
            val result =
                mockMvc.patch("/point/$userId/use") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(amount)
                }

            // Then
            result.andExpect {
                status { isOk() }
                jsonPath("$.id") { value(0L) }
                jsonPath("$.point") { value(2000L) }
                jsonPath("$.updateMillis") { value(100L) }
            }
        }
    }
}
