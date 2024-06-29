package com.hhplus.lecture.domain

import com.hhplus.lecture.application.ApplyHistoryManager
import com.hhplus.lecture.application.repository.ApplyHistoryRepository
import com.hhplus.lecture.common.exception.errors.ApplyHistoryException
import com.hhplus.lecture.common.type.ApplyStatus
import com.hhplus.lecture.domain.entity.ApplyHistory
import com.hhplus.lecture.domain.entity.Lecture
import com.hhplus.lecture.domain.entity.Student
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime

class ApplyHistoryManagerTest {
    @Mock
    private lateinit var applyHistoryRepository: ApplyHistoryRepository

    @InjectMocks
    private lateinit var applyHistoryManager: ApplyHistoryManager

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        applyHistoryRepository.deleteAll()
    }

    @Test
    fun `이미 강의에 신청한 학생이 신청한경우 예외를 리턴한다`() {
        // given
        val studentId = 0L
        val lectureId = 0L
        val student = Student("Student")
        val lecture =
            Lecture(
                title = LectureManagerTest.LECTURE_TITLE,
                applyStartAt = LocalDateTime.of(2024, 6, 20, 13, 0),
                lectureAt = LocalDateTime.of(2024, 6, 30, 13, 0),
            )
        val applyHistories =
            listOf(
                ApplyHistory(
                    student = student,
                    lecture = lecture,
                    applyStatus = ApplyStatus.COMPLETED,
                ),
            )
        `when`(
            applyHistoryRepository.findByStudentIdAndLectureId(
                studentId = studentId,
                lectureId = lectureId,
            ),
        ).thenReturn(applyHistories)

        // when
        val exception =
            assertThrows<ApplyHistoryException.AlreadyApplied> {
                applyHistoryManager.checkApplyStatus(studentId = studentId, lectureId = lectureId)
            }

        // then
        assertThat(exception)
            .message()
            .contains("Already applied")
    }

    @Test
    fun `강의 신청내역은 있으나, 실패한 이력만 있는 경우 예외를 리턴하지 않는다`() {
        // given
        val studentId = 0L
        val lectureId = 0L
        val student = Student("Student")
        val lecture =
            Lecture(
                title = LectureManagerTest.LECTURE_TITLE,
                applyStartAt = LocalDateTime.of(2024, 6, 20, 13, 0),
                lectureAt = LocalDateTime.of(2024, 6, 30, 13, 0),
            )
        val applyHistories =
            listOf(
                ApplyHistory(
                    student = student,
                    lecture = lecture,
                    applyStatus = ApplyStatus.FAILED,
                ),
            )
        `when`(
            applyHistoryRepository.findByStudentIdAndLectureId(
                studentId = studentId,
                lectureId = lectureId,
            ),
        ).thenReturn(applyHistories)

        // when, then
        assertDoesNotThrow {
            applyHistoryManager.checkApplyStatus(studentId = studentId, lectureId = lectureId)
        }
    }

    @Test
    fun `강의 신청 내역 자체가 없는 경우, 예외를 리턴하지 않는다`() {
        // given
        val studentId = 0L
        val lectureId = 0L
        val applyHistories = emptyList<ApplyHistory>()

        `when`(
            applyHistoryRepository.findByStudentIdAndLectureId(
                studentId = studentId,
                lectureId = lectureId,
            ),
        ).thenReturn(applyHistories)

        // when, then
        assertDoesNotThrow {
            applyHistoryManager.checkApplyStatus(studentId = studentId, lectureId = lectureId)
        }
    }
}
