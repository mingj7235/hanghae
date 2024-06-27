package com.hhplus.lecture.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.hhplus.lecture.application.LectureApplyService
import com.hhplus.lecture.application.repository.ApplyHistoryRepository
import com.hhplus.lecture.application.repository.LectureRepository
import com.hhplus.lecture.application.repository.StudentRepository
import com.hhplus.lecture.common.type.ApplyStatus
import com.hhplus.lecture.controller.request.LectureApplyRequest
import com.hhplus.lecture.domain.entity.ApplyHistory
import com.hhplus.lecture.domain.entity.Lecture
import com.hhplus.lecture.domain.entity.Student
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.event.annotation.AfterTestExecution
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * Lecture Controller 통합 테스트
 * - 각 API 의 통합테스트를 진행한다.
 * - 동시성 이슈 테스트를 진행한다.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
@TestPropertySource(locations = ["classpath:application-test.yml"])
class LectureApplyControllerTest(
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val lectureApplyService: LectureApplyService,
    @Autowired private val lectureRepository: LectureRepository,
    @Autowired private val studentRepository: StudentRepository,
    @Autowired private val applyHistoryRepository: ApplyHistoryRepository,
) {
    @BeforeEach
    fun setUp() {
        studentRepository.save(
            Student(
                "Student",
            ),
        )
        // 수강 신청 가능한 강의
        lectureRepository.save(
            Lecture(
                title = "Lecture",
                applyStartAt = LocalDateTime.now().minusDays(1),
                lectureAt = LocalDateTime.now().plusDays(5),
            ),
        )
        // 아직 수강신청이 열리지 않은 강의
        lectureRepository.save(
            Lecture(
                title = "NotOpenedLecture",
                applyStartAt = LocalDateTime.now().plusDays(1),
                lectureAt = LocalDateTime.now().plusDays(5),
            ),
        )
        // 이미 종료된 강의
        lectureRepository.save(
            Lecture(
                title = "ClosedLecture",
                applyStartAt = LocalDateTime.now().minusDays(5),
                lectureAt = LocalDateTime.now().minusDays(1),
            ),
        )
    }

    @AfterTestExecution
    fun deleteAll() {
        lectureRepository.deleteAll()
        studentRepository.deleteAll()
        applyHistoryRepository.deleteAll()
    }

    @Nested
    @DisplayName("[apply] 특강 수강 신청 API 통합 테스트")
    open inner class LectureApplyTest {
        @Test
        fun `존재하지 않는 학생이 특강을 신청했을 경우 예외가 발생한다`() {
            val nonExistStudentId = NON_EXISTED_ID
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
                    status { isBadRequest() }
                    jsonPath("$.message") { value("Not found student") }
                }
        }

        @Test
        fun `존재하지 않는 특강을 신청했을 경우 예외가 발생한다`() {
            val studentId = 1L
            val lectureId = NON_EXISTED_ID

            val applyRequest =
                LectureApplyRequest.Apply(
                    studentId = studentId,
                    lectureId = lectureId,
                )

            mockMvc
                .post("/lectures/apply") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(applyRequest)
                }.andExpect {
                    status { isBadRequest() }
                    jsonPath("$.message") { value("Not found lecture") }
                }
        }

        @Test
        @Transactional
        @Rollback
        fun `특강 신청 시작 시점 전에 신청을 했다면 예외가 발생한다`() {
            val studentId = 1L
            val lectureId = 2L // NotOpenedLecture

            val applyRequest =
                LectureApplyRequest.Apply(
                    studentId = studentId,
                    lectureId = lectureId,
                )

            mockMvc
                .post("/lectures/apply") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(applyRequest)
                }.andExpect {
                    status { isBadRequest() }
                    jsonPath("$.message") { value("Invalid lecture apply date time") }
                }
        }

        @Test
        @Transactional
        @Rollback
        fun `이미 종료된 강의를 신청을 했다면 예외가 발생한다`() {
            val studentId = 1L
            val lectureId = 3L // ClosedLecture

            val applyRequest =
                LectureApplyRequest.Apply(
                    studentId = studentId,
                    lectureId = lectureId,
                )

            mockMvc
                .post("/lectures/apply") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(applyRequest)
                }.andExpect {
                    status { isBadRequest() }
                    jsonPath("$.message") { value("Invalid lecture apply date time") }
                }
        }

        @Test
        fun `이미 강의를 성공적으로 등록한 학생이 다시 등록한 경우 등록에 실패한다 그리고 실패 기록이 저장된다`() {
            val student = studentRepository.save(Student("Student10"))
            val lecture =
                lectureRepository.save(
                    Lecture(
                        title = "Lecture10",
                        applyStartAt = LocalDateTime.now().minusDays(1),
                        lectureAt = LocalDateTime.now().plusDays(5),
                    ),
                )

            applyHistoryRepository.save(
                ApplyHistory(
                    student,
                    lecture,
                    applyStatus = ApplyStatus.COMPLETED,
                ),
            )

            val applyRequest =
                LectureApplyRequest.Apply(
                    studentId = student.id,
                    lectureId = lecture.id,
                )

            mockMvc
                .post("/lectures/apply") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(applyRequest)
                }.andExpect {
                    status { isOk() }
                    jsonPath("$.lectureTitle") { value(null) }
                    jsonPath("$.result") { value(false) }
                    jsonPath("$.failedReason") { value("Already applied") }

                    val applyHistory = applyHistoryRepository.findByStudentIdAndLectureId(student.id, lecture.id)
                    assertThat(applyHistory.size).isEqualTo(2)
                    assertThat(applyHistory[1].applyStatus).isEqualTo(ApplyStatus.FAILED)
                }
        }

        @Test
        fun `정원이 마감된 특강을 신청했을 경우 예외가 발생한다 그리고 실패 기록이 저장된다`() {
            val lecture =
                Lecture(
                    title = "OverbookedLecture",
                    applyStartAt = LocalDateTime.now().minusDays(1),
                    lectureAt = LocalDateTime.now().plusDays(5),
                )
            lecture.updateCapacity(1)
            lecture.increaseCurrentEnrollmentCount()
            val savedLecture = lectureRepository.save(lecture)

            val studentId = 1L
            val lectureId = savedLecture.id

            val applyRequest =
                LectureApplyRequest.Apply(
                    studentId = studentId,
                    lectureId = lectureId,
                )

            mockMvc
                .post("/lectures/apply") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(applyRequest)
                }.andExpect {
                    status { isOk() }
                    jsonPath("$.lectureTitle") { value(null) }
                    jsonPath("$.result") { value(false) }
                    jsonPath("$.failedReason") { value("Lecture is fully booked") }

                    val applyHistory = applyHistoryRepository.findByStudentIdAndLectureId(studentId, lectureId)
                    assertThat(applyHistory.size).isEqualTo(1)
                    assertThat(applyHistory[0].applyStatus).isEqualTo(ApplyStatus.FAILED)
                }
        }

        @Test
        @Transactional
        @Rollback
        fun `수강신청이 성공한다면 applyHistory 에도 성공한 기록이 올바르게 저장된다`() {
            // given
            val studentId = 1L
            val lectureId = 1L

            val applyRequest =
                LectureApplyRequest.Apply(
                    studentId = studentId,
                    lectureId = lectureId,
                )

            // when, then
            mockMvc
                .post("/lectures/apply") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(applyRequest)
                }.andExpect {
                    status { isOk() }
                    jsonPath("$.result") { value(true) }
                    jsonPath("$.lectureTitle") { value("Lecture") }

                    val applyHistory = applyHistoryRepository.findByStudentIdAndLectureId(studentId, lectureId)
                    assertThat(applyHistory[0].applyStatus).isEqualTo(ApplyStatus.COMPLETED)
                }
        }
    }

    @Nested
    @DisplayName("[getLectures] 강의 리스트 조회 API 테스트")
    inner class GetLecturesTest {
        @Test
        fun `현재 수강신청 가능한 강의 1개, 앞으로 열릴 강의 1개, 종료된 강의 1개가 있다면 종료된 강의를 제외한 다른 2개의 강의를 리턴한다 `() {
            mockMvc
                .get("/lectures")
                .andExpect {
                    status { isOk() }
                    jsonPath("$.size()") { value(2) }
                    jsonPath("$[0].lectureTitle") { value("Lecture") }
                    jsonPath("$[1].lectureTitle") { value("NotOpenedLecture") }
                }
        }

        @Test
        fun `현재 수강 신청이 가능하거나 열리지 않은 강의가 없다면 예외를 리턴한다`() {
            lectureRepository.deleteAll()

            mockMvc
                .get("/lectures")
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.message") { value("Not found lecture") }
                }
        }
    }

    @Nested
    @DisplayName("[getApplyStatus] 강의 수강 신청 여부 확인 API 테스트")
    inner class GetApplyStatusTest {
        @Test
        @Transactional
        @Rollback
        fun `수강 신청을 성공 한 내역이 있다면 성공을 반환한다`() {
            val student = studentRepository.save(Student("Student"))
            val lecture =
                lectureRepository.save(
                    Lecture(
                        title = "Lecture",
                        applyStartAt = LocalDateTime.now().minusDays(1),
                        lectureAt = LocalDateTime.now().plusDays(5),
                    ),
                )

            applyHistoryRepository.save(
                ApplyHistory(
                    student,
                    lecture,
                    applyStatus = ApplyStatus.COMPLETED,
                ),
            )

            val lectureId = lecture.id
            val studentId = student.id

            mockMvc
                .get("/lectures/$lectureId/application/$studentId")
                .andExpect {
                    status { isOk() }
                    jsonPath("$.lectureId") { value(lectureId) }
                    jsonPath("$.lectureTitle") { value(lecture.title) }
                    jsonPath("$.applyStatus") { value("COMPLETED") }
                }
        }

        @Test
        @Transactional
        @Rollback
        fun `수강 신청을 실패한 내역만 있다면 실패를 반환한다`() {
            val student = studentRepository.save(Student("Student"))
            val lecture =
                lectureRepository.save(
                    Lecture(
                        title = "Lecture",
                        applyStartAt = LocalDateTime.now().minusDays(1),
                        lectureAt = LocalDateTime.now().plusDays(5),
                    ),
                )

            applyHistoryRepository.save(
                ApplyHistory(
                    student,
                    lecture,
                    applyStatus = ApplyStatus.FAILED,
                ),
            )

            val lectureId = lecture.id
            val studentId = student.id

            mockMvc
                .get("/lectures/$lectureId/application/$studentId")
                .andExpect {
                    status { isOk() }
                    jsonPath("$.lectureId") { value(lectureId) }
                    jsonPath("$.lectureTitle") { value(lecture.title) }
                    jsonPath("$.applyStatus") { value("FAILED") }
                }
        }

        @Test
        fun `수강 신청한 내역이 없다면 실패를 반환한다`() {
            mockMvc
                .get("/lectures/1/application/1")
                .andExpect {
                    status { isOk() }
                    jsonPath("$.lectureId") { value(1) }
                    jsonPath("$.lectureTitle") { value("Lecture") }
                    jsonPath("$.applyStatus") { value("FAILED") }
                }
        }
    }

    companion object {
        const val NON_EXISTED_ID = -1L
    }
}
