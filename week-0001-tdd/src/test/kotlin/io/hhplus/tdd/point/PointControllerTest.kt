package io.hhplus.tdd.point

import io.hhplus.tdd.point.dto.PointServiceDto
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
    @DisplayName("특정 유저의 포인트를 조회한다.")
    fun `getUserPointApiTest`() {
        // Given
        given(
            pointService.getPointBy(1),
        ).willReturn(
            PointServiceDto.Point(
                id = 1,
                point = 0,
                updateMillis = 0,
            ),
        )

        // When
        val result = mockMvc.get("/point/1")

        // Then
        result.andExpect {
            status { isOk() }
            jsonPath("$.id") { value(1) }
            jsonPath("$.point") { value(0) }
            jsonPath("$.updateMillis") { value(0) }
        }
    }

    @Test
    @DisplayName("특정 유저의 포인트 충전 및 이용 내역을 조회한다.")
    fun `getUserPointHistoryApiTest`() {
        val result =
            mockMvc.get("/point/1/histories") {
                accept = APPLICATION_JSON
            }
        result.andExpect {
            jsonPath("$.length()") { value(0) }
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
