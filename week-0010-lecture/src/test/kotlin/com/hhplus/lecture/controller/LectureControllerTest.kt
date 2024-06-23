package com.hhplus.lecture.controller

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest

/**
 * Lecture Controller 통합 테스트
 */
@SpringBootTest
@AutoConfigureMockMvc
class LectureControllerTest {
    @Nested
    @DisplayName("[apply] 특강 수강 신청 API 통합 테스트")
    inner class LectureApplyTest {
        @Test
        fun `존재하지 않는 학생이 특강을 신청했을 경우 예외가 발생한다`() {
            TODO()
        }

        @Test
        fun `특강 신청 시작 시점 전에 신청을 했다면 예외가 발생한다`() {
            TODO()
        }

        @Test
        fun `특강시간 이후에 신청을 했다면 예외가 발생한다`() {
            TODO()
        }

        @Test
        fun `정원이 마감된 특강을 신청했을 경우 예외가 발생한다`() {
            TODO()
        }

        @Test
        fun `특강을 이미 신청하여 등록이 된 학생이 한 번 더 신청할 경우 예외가 발생한다`() {
            TODO()
        }
    }
}
