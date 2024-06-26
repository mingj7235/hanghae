package com.hhplus.lecture.domain

import com.hhplus.lecture.application.LectureManager
import com.hhplus.lecture.application.repository.LectureRepository
import com.hhplus.lecture.common.exception.errors.LectureException
import com.hhplus.lecture.domain.entity.Lecture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.time.LocalDateTime

/**
 * LectureManager 단위 테스트
 */
class LectureManagerTest {
    @Mock
    private lateinit var lectureRepository: LectureRepository

    @InjectMocks
    private lateinit var lectureManager: LectureManager

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @AfterEach
    fun deleteAll() {
        lectureRepository.deleteAll()
    }

    @Test
    fun `존재하지 않는 lectureId 로 강의를 조회하려고 하는 경우 예외를 리턴한다`() {
        `when`(lectureRepository.findById(NON_EXISTED_LECTURE_ID)).thenReturn(null)

        val exception =
            assertThrows<LectureException.LectureNotfound> {
                lectureManager.findAvailableById(NON_EXISTED_LECTURE_ID)
            }

        assertThat(exception)
            .message()
            .contains("Not found lecture")
    }

    @Test
    fun `현재 시간이 수강 신청 시간 전이라면 예외를 리턴한다`() {
        val lectureId = 0L
        val lecture =
            Lecture(
                title = LECTURE_TITLE,
                applyStartAt = LocalDateTime.of(2024, 6, 27, 13, 0),
                lectureAt = LocalDateTime.of(2024, 6, 30, 13, 0),
                capacity = LECTURE_CAPACITY,
            )

        `when`(lectureRepository.findById(lectureId)).thenReturn(lecture)

        val exception =
            assertThrows<LectureException.InvalidLectureApplyDateTime> {
                lectureManager.findAvailableById(lectureId)
            }

        assertThat(exception)
            .message()
            .contains("Invalid lecture apply date time")
    }

    @Test
    fun `현재 시간이 특강 시간 후라면 예외를 리턴한다`() {
        val lectureId = 0L
        val lecture =
            Lecture(
                title = LECTURE_TITLE,
                applyStartAt = LocalDateTime.of(2024, 6, 20, 13, 0),
                lectureAt = LocalDateTime.of(2024, 6, 25, 13, 0),
                capacity = LECTURE_CAPACITY,
            )

        `when`(lectureRepository.findById(lectureId)).thenReturn(lecture)

        val exception =
            assertThrows<LectureException.InvalidLectureApplyDateTime> {
                lectureManager.findAvailableById(lectureId)
            }

        assertThat(exception)
            .message()
            .contains("Invalid lecture apply date time")
    }

    @Test
    fun `수강 신청이 가능한 강의를 수강신청 한다면, 그 강의를 리턴한다`() {
        val lectureId = 0L
        val lecture =
            Lecture(
                title = LECTURE_TITLE,
                applyStartAt = LocalDateTime.of(2024, 6, 20, 13, 0),
                lectureAt = LocalDateTime.of(2024, 6, 30, 13, 0),
                capacity = 30,
            )

        `when`(lectureRepository.findById(lectureId)).thenReturn(lecture)

        val availableLecture = lectureManager.findAvailableById(lectureId)

        assertThat(availableLecture.id).isEqualTo(lectureId)
        assertThat(availableLecture.title).isEqualTo(LECTURE_TITLE)
        assertThat(availableLecture.capacity).isEqualTo(LECTURE_CAPACITY)
    }

    @Test
    fun `수강인원이 꽉 찬 강의를 수강 신청하면 예외를 리턴한다`() {
        val lectureId = 0L
        val lecture =
            Lecture(
                title = LECTURE_TITLE,
                applyStartAt = LocalDateTime.of(2024, 6, 20, 13, 0),
                lectureAt = LocalDateTime.of(2024, 6, 30, 13, 0),
                capacity = 1,
            )

        lecture.increaseCurrentEnrollmentCount()

        `when`(lectureRepository.findById(lectureId)).thenReturn(lecture)

        val exception =
            assertThrows<LectureException.EnrollmentFull> {
                lectureManager.checkCurrentEnrollmentCount(lecture)
            }

        assertThat(exception)
            .message()
            .contains("Lecture is fully booked")
    }

    companion object {
        const val NON_EXISTED_LECTURE_ID = -1L
        const val LECTURE_TITLE = "TEST TITLE"
        const val LECTURE_CAPACITY = 30
    }
}
