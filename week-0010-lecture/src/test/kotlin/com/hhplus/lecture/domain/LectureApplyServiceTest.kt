package com.hhplus.lecture.domain

import com.hhplus.lecture.application.ApplyHistoryManager
import com.hhplus.lecture.application.LectureApplyService
import com.hhplus.lecture.application.LectureManager
import com.hhplus.lecture.application.StudentManager
import com.hhplus.lecture.application.dto.LectureApplyServiceDto
import com.hhplus.lecture.common.exception.errors.ApplyHistoryException
import com.hhplus.lecture.common.exception.errors.LectureException
import com.hhplus.lecture.common.type.ApplyStatus
import com.hhplus.lecture.common.type.LectureStatus
import com.hhplus.lecture.domain.entity.Lecture
import com.hhplus.lecture.domain.entity.Student
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime

/**
 * LectureApplyService 단위 테스트
 */
class LectureApplyServiceTest {
    @Mock
    private lateinit var lectureManager: LectureManager

    @Mock
    private lateinit var studentManager: StudentManager

    @Mock
    private lateinit var applyHistoryManager: ApplyHistoryManager

    @InjectMocks
    private lateinit var lectureApplyService: LectureApplyService

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Nested
    @DisplayName("LectureApplyService 의 apply 메서드를 테스트한다.")
    inner class ApplyTest {
        @Test
        fun `수강 신청에 성공한다`() {
            // given
            val studentId = 0L
            val lectureId = 0L
            val applyDto = LectureApplyServiceDto.Apply(studentId, lectureId)
            val student = Student("Student")
            val lecture =
                Lecture(
                    title = LECTURE_TITLE,
                    applyStartAt = LocalDateTime.of(2024, 6, 20, 13, 0),
                    lectureAt = LocalDateTime.of(2024, 6, 30, 13, 0),
                )

            `given`(studentManager.findById(studentId)).willReturn(student)
            `given`(lectureManager.findAvailableById(lectureId)).willReturn(lecture)

            // when
            doNothing().`when`(applyHistoryManager).checkApplyStatus(studentId, lectureId)
            doNothing().`when`(lectureManager).checkCurrentEnrollmentCount(lecture)
            doNothing().`when`(applyHistoryManager).save(student, lecture, ApplyStatus.COMPLETED)

            // then
            val applyResult = lectureApplyService.apply(applyDto)
            assertThat(applyResult.lectureTitle).isEqualTo(LECTURE_TITLE)
            assertThat(applyResult.result).isTrue()
            verify(lectureManager).findAvailableById(lectureId)
            verify(studentManager).findById(studentId)
            verify(applyHistoryManager).checkApplyStatus(studentId, lectureId)
            verify(lectureManager).checkCurrentEnrollmentCount(lecture)
            verify(applyHistoryManager).save(student, lecture, ApplyStatus.COMPLETED)
        }

        @Test
        fun `이미 해당 강의를 신청한 이력이 있다면 강의 신청에 실패한다`() {
            // given
            val studentId = 0L
            val lectureId = 0L
            val applyDto = LectureApplyServiceDto.Apply(studentId, lectureId)
            val student = Student("Student")
            val lecture =
                Lecture(
                    title = LectureManagerTest.LECTURE_TITLE,
                    applyStartAt = LocalDateTime.of(2024, 6, 20, 13, 0),
                    lectureAt = LocalDateTime.of(2024, 6, 30, 13, 0),
                )

            `given`(studentManager.findById(studentId)).willReturn(student)
            `given`(lectureManager.findAvailableById(lectureId)).willReturn(lecture)

            // when
            doThrow(ApplyHistoryException.AlreadyApplied()).`when`(applyHistoryManager).checkApplyStatus(studentId, lectureId)

            // then
            val applyResult = lectureApplyService.apply(applyDto)
            assertFalse(applyResult.result)
            verify(lectureManager).findAvailableById(lectureId)
            verify(studentManager).findById(studentId)
            verify(applyHistoryManager).checkApplyStatus(studentId, lectureId)
            verify(applyHistoryManager).save(student, lecture, ApplyStatus.FAILED)
        }

        @Test
        fun `수강 신청인원이 이미 꽉 찬 경우 강의 신청에 실패한다`() {
            // given
            val studentId = 0L
            val lectureId = 0L
            val applyDto = LectureApplyServiceDto.Apply(studentId, lectureId)
            val student = Student("Student")
            val lecture =
                Lecture(
                    title = LECTURE_TITLE,
                    applyStartAt = LocalDateTime.of(2024, 6, 20, 13, 0),
                    lectureAt = LocalDateTime.of(2024, 6, 30, 13, 0),
                )

            `given`(studentManager.findById(studentId)).willReturn(student)
            `given`(lectureManager.findAvailableById(lectureId)).willReturn(lecture)

            // when
            doNothing().`when`(applyHistoryManager).checkApplyStatus(studentId, lectureId)
            doThrow(LectureException.EnrollmentFull()).`when`(lectureManager).checkCurrentEnrollmentCount(lecture)

            // then
            val applyResult = lectureApplyService.apply(applyDto)
            assertFalse(applyResult.result)
            verify(lectureManager).findAvailableById(lectureId)
            verify(studentManager).findById(studentId)
            verify(applyHistoryManager).checkApplyStatus(studentId, lectureId)
            verify(lectureManager).checkCurrentEnrollmentCount(lecture)
            verify(applyHistoryManager).save(student, lecture, ApplyStatus.FAILED)
        }
    }

    @Nested
    @DisplayName("LectureApplyService 의 getLectures 메서드를 테스트한다.")
    inner class GetLecturesTest {
        @Test
        fun `정원이 꽉찬 강의 1개와 아직 수강 신청이 가능한 강의 두개가 DB 에 저장되어 있는 경우, 2개의 강의 목록을 리턴한다`() {
            // given
            val fullLecture =
                Lecture(
                    title = "Full Lecture",
                    applyStartAt = LocalDateTime.now().minusDays(1),
                    lectureAt = LocalDateTime.now().plusDays(1),
                )

            fullLecture.updateCapacity(1)
            fullLecture.increaseCurrentEnrollmentCount()

            val openLecture1 =
                Lecture(
                    title = "Open Lecture",
                    applyStartAt = LocalDateTime.now().minusDays(1),
                    lectureAt = LocalDateTime.now().plusDays(1),
                )

            `given`(lectureManager.findUpcomingLectures()).willReturn(listOf(fullLecture, openLecture1))

            // when
            val lectures = lectureApplyService.getLectures()

            // then
            assertThat(lectures.size).isEqualTo(2)
            assertThat(lectures[0].lectureStatus).isEqualTo(LectureStatus.CLOSED)
            assertThat(lectures[0].lectureTitle).isEqualTo("Full Lecture")
            assertThat(lectures[1].lectureStatus).isEqualTo(LectureStatus.OPENED)
            assertThat(lectures[1].lectureTitle).isEqualTo("Open Lecture")
        }

        @Test
        fun `현재 등록된 강의가 없는 경우 예외를 리턴한다`() {
            `given`(lectureManager.findUpcomingLectures()).willReturn(emptyList())

            // when
            val exception =
                assertThrows<LectureException.LectureNotfound> {
                    lectureApplyService.getLectures()
                }

            assertThat(exception)
                .message()
                .contains("Not found lecture")
        }
    }

    @Nested
    @DisplayName("LectureApplyService 의 getApplyStatus 메서드를 테스트한다.")
    inner class GetApplyStatusTest {
        @Test
        fun `학생이 수강신청을 한 내역중에 성공이 있다면 성공을 반환한다`() {
            // given
            val studentId = 0L
            val lectureId = 0L
            val student = Student("Student")
            val lecture =
                Lecture(
                    title = LECTURE_TITLE,
                    applyStartAt = LocalDateTime.of(2024, 6, 20, 13, 0),
                    lectureAt = LocalDateTime.of(2024, 6, 30, 13, 0),
                )

            `given`(studentManager.findById(studentId)).willReturn(student)
            `given`(lectureManager.findAvailableById(lectureId)).willReturn(lecture)
            `given`(applyHistoryManager.hasApplied(studentId, lectureId)).willReturn(true)

            // when
            val result = lectureApplyService.getApplyStatus(lectureId, studentId)

            // then
            assertThat(result.lectureId).isEqualTo(lectureId)
            assertThat(result.lectureTitle).isEqualTo(LECTURE_TITLE)
            assertThat(result.applyStatus).isEqualTo(ApplyStatus.COMPLETED)

            verify(lectureManager).findAvailableById(lectureId)
            verify(studentManager).findById(studentId)
            verify(applyHistoryManager).hasApplied(studentId, lectureId)
        }

        @Test
        fun `학생이 수강신청을 한 내역중에 실패만 있다면 실패를 반환한다`() {
            // given
            val studentId = 0L
            val lectureId = 0L
            val student = Student("Student")
            val lecture =
                Lecture(
                    title = LECTURE_TITLE,
                    applyStartAt = LocalDateTime.of(2024, 6, 20, 13, 0),
                    lectureAt = LocalDateTime.of(2024, 6, 30, 13, 0),
                )

            `given`(studentManager.findById(studentId)).willReturn(student)
            `given`(lectureManager.findAvailableById(lectureId)).willReturn(lecture)
            `given`(applyHistoryManager.hasApplied(studentId, lectureId)).willReturn(false)

            // when
            val result = lectureApplyService.getApplyStatus(lectureId, studentId)

            // then
            assertThat(result.lectureId).isEqualTo(lectureId)
            assertThat(result.lectureTitle).isEqualTo(LECTURE_TITLE)
            assertThat(result.applyStatus).isEqualTo(ApplyStatus.FAILED)

            verify(lectureManager).findAvailableById(lectureId)
            verify(studentManager).findById(studentId)
            verify(applyHistoryManager).hasApplied(studentId, lectureId)
        }
    }

    companion object {
        const val LECTURE_TITLE = "TEST TITLE"
    }
}
