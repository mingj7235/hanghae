package com.hhplus.lecture.domain

import com.hhplus.lecture.application.ApplyHistoryManager
import com.hhplus.lecture.application.LectureApplyService
import com.hhplus.lecture.application.LectureManager
import com.hhplus.lecture.application.StudentManager
import com.hhplus.lecture.application.dto.LectureApplyServiceDto
import com.hhplus.lecture.common.exception.errors.ApplyHistoryException
import com.hhplus.lecture.common.exception.errors.LectureException
import com.hhplus.lecture.common.type.ApplyStatus
import com.hhplus.lecture.domain.entity.Lecture
import com.hhplus.lecture.domain.entity.Student
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
                capacity = LECTURE_CAPACITY,
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
                capacity = LectureManagerTest.LECTURE_CAPACITY,
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
                capacity = LECTURE_CAPACITY,
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

    companion object {
        const val LECTURE_TITLE = "TEST TITLE"
        const val LECTURE_CAPACITY = 30
    }
}
