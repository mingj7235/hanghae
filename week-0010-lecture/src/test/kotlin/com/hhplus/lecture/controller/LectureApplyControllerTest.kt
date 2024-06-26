package com.hhplus.lecture.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.hhplus.lecture.application.LectureApplyService
import com.hhplus.lecture.controller.request.LectureApplyRequest
import com.hhplus.lecture.infra.repository.jpa.JpaApplyHistoryRepository
import com.hhplus.lecture.infra.repository.jpa.JpaLectureRepository
import com.hhplus.lecture.infra.repository.jpa.JpaStudentRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

/**
 * Lecture Controller 통합 테스트
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = ["classpath:application-test.yml"])
class LectureApplyControllerTest(
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val lectureApplyService: LectureApplyService,
    @Autowired private val jpaLectureRepository: JpaLectureRepository,
    @Autowired private val jpaStudentRepository: JpaStudentRepository,
    @Autowired private val jpaApplyHistoryRepository: JpaApplyHistoryRepository,
) {
    @BeforeEach
    fun setup() {
        jpaLectureRepository.deleteAll()
        jpaStudentRepository.deleteAll()
        jpaApplyHistoryRepository.deleteAll()
    }

    @Nested
    @DisplayName("[apply] 특강 수강 신청 API 통합 테스트")
    inner class LectureApplyTest {
        @Test
        fun `존재하지 않는 학생이 특강을 신청했을 경우 예외가 발생한다`() {
            val nonExistStudentId = NON_EXISTED_STUDENT_ID
            val lectureId = 0L

            val applyRequest =
                LectureApplyRequest.Apply(
                    studentId = nonExistStudentId,
                    lectureId = lectureId,
                )

            mockMvc
                .post("/lectures/apply") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(applyRequest)
                }.andExpect {
                    status { isNotFound() }
                    jsonPath("$.message") { value("Not found user. [id] = [$NON_EXISTED_STUDENT_ID]") }
                }
        }

        @Test
        fun `존재하지 않는 특강을 신청했을 경우 예외가 발생한다`() {
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

        @Test
        fun `수강신청이 성공한다`() {
            val applyRequest =
                LectureApplyRequest.Apply(
                    studentId = 0L,
                    lectureId = 0L,
                )

            mockMvc
                .post("/lectures/apply") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(applyRequest)
                }.andExpect {
                    status { isOk() }
                    jsonPath("$.result") { value(true) }
                }
        }
    }

    companion object {
        const val NON_EXISTED_STUDENT_ID = -1L
    }
}
