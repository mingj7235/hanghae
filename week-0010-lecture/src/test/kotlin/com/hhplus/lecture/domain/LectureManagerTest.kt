package com.hhplus.lecture.domain

import com.hhplus.lecture.application.LectureManager
import com.hhplus.lecture.application.repository.LectureRepository
import com.hhplus.lecture.common.exception.errors.LectureException
import com.hhplus.lecture.domain.entity.Lecture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.BDDMockito.given
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

    @Nested
    @DisplayName("LectureManager 의 findAvailableById 를 테스트한다.")
    inner class FindAvailableByIdTest {
        @Test
        fun `존재하지 않는 lectureId 로 강의를 조회하려고 하는 경우 예외를 리턴한다`() {
            `when`(lectureRepository.findByLectureIdWithPessimisticLock(NON_EXISTED_LECTURE_ID)).thenReturn(null)

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
                    applyStartAt = LocalDateTime.now().plusDays(1),
                    lectureAt = LocalDateTime.now().plusDays(5),
                )

            `when`(lectureRepository.findByLectureIdWithPessimisticLock(lectureId)).thenReturn(lecture)

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
                    applyStartAt = LocalDateTime.now().minusDays(5),
                    lectureAt = LocalDateTime.now().minusDays(3),
                )

            `when`(lectureRepository.findByLectureIdWithPessimisticLock(lectureId)).thenReturn(lecture)

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
                    applyStartAt = LocalDateTime.now().minusDays(1),
                    lectureAt = LocalDateTime.now().plusDays(5),
                )

            `when`(lectureRepository.findByLectureIdWithPessimisticLock(lectureId)).thenReturn(lecture)

            val availableLecture = lectureManager.findAvailableById(lectureId)

            assertThat(availableLecture.id).isEqualTo(lectureId)
            assertThat(availableLecture.title).isEqualTo(LECTURE_TITLE)
            assertThat(availableLecture.capacity).isEqualTo(LECTURE_CAPACITY)
        }
    }

    @Nested
    @DisplayName("LectureManager 의 checkCurrentEnrollmentCount 를 테스트한다.")
    inner class CheckCurrentEnrollmentCountTest {
        @Test
        fun `수강인원이 꽉 찬 강의를 수강 신청하면 예외를 리턴한다`() {
            val lectureId = 0L
            val lecture =
                Lecture(
                    title = LECTURE_TITLE,
                    applyStartAt = LocalDateTime.now().minusDays(1),
                    lectureAt = LocalDateTime.now().plusDays(5),
                )
            lecture.updateCapacity(1)
            lecture.increaseCurrentEnrollmentCount()

            `when`(lectureRepository.findByLectureIdWithPessimisticLock(lectureId)).thenReturn(lecture)

            val exception =
                assertThrows<LectureException.EnrollmentFull> {
                    lectureManager.checkCurrentEnrollmentCount(lecture)
                }

            assertThat(exception)
                .message()
                .contains("Lecture is fully booked")
        }
    }

    @Nested
    @DisplayName("LectureManager 의 findUpcomingLectures 을 테스트한다.")
    inner class FindUpcomingLecturesTest {
        @Test
        fun `이미 종료된 강의 1개, 아직 시작하지 않은 강의가 2개가 DB 에 저장되어있다면 사작하지 않은 강의 2개만을 리턴한다`() {
            // given
            val finishedLecture =
                Lecture(
                    title = "Finished Lecture",
                    applyStartAt = LocalDateTime.now().minusDays(5),
                    lectureAt = LocalDateTime.now().minusDays(1),
                )

            val upcomingLecture1 =
                Lecture(
                    title = "Upcoming Lecture1",
                    applyStartAt = LocalDateTime.now().minusDays(5),
                    lectureAt = LocalDateTime.now().plusDays(1),
                )

            val upcomingLecture2 =
                Lecture(
                    title = "Upcoming Lecture2",
                    applyStartAt = LocalDateTime.now().minusDays(5),
                    lectureAt = LocalDateTime.now().plusDays(1),
                )

            `given`(lectureRepository.findAll()).willReturn(listOf(finishedLecture, upcomingLecture1, upcomingLecture2))

            // when
            val upcomingLectures = lectureManager.findUpcomingLectures()

            // then
            assertThat(upcomingLectures.size).isEqualTo(2)
            assertThat(upcomingLectures[0].title).isEqualTo("Upcoming Lecture1")
            assertThat(upcomingLectures[1].title).isEqualTo("Upcoming Lecture2")
        }

        @Test
        fun `이미 종료된 강의만이 DB 에 저장되어 있다면 빈 리스트를 리턴한다`() {
            // given
            val finishedLecture =
                Lecture(
                    title = "Finished Lecture",
                    applyStartAt = LocalDateTime.now().minusDays(5),
                    lectureAt = LocalDateTime.now().minusDays(1),
                )

            `given`(lectureRepository.findAll()).willReturn(listOf(finishedLecture))

            // when
            val upcomingLectures = lectureManager.findUpcomingLectures()

            assertThat(upcomingLectures.size).isEqualTo(0)
        }
    }

    companion object {
        const val NON_EXISTED_LECTURE_ID = -1L
        const val LECTURE_TITLE = "TEST TITLE"
        const val LECTURE_CAPACITY = 30
    }
}
