package com.hhplus.lecture.integration

import com.hhplus.lecture.application.LectureApplyService
import com.hhplus.lecture.application.dto.LectureApplyServiceDto
import com.hhplus.lecture.application.repository.ApplyHistoryRepository
import com.hhplus.lecture.application.repository.LectureRepository
import com.hhplus.lecture.application.repository.StudentRepository
import com.hhplus.lecture.common.type.ApplyStatus
import com.hhplus.lecture.domain.entity.Lecture
import com.hhplus.lecture.domain.entity.Student
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.event.annotation.AfterTestExecution
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.yml"])
class LectureApplyConcurrencyTest(
    @Autowired private val lectureApplyService: LectureApplyService,
    @Autowired private val lectureRepository: LectureRepository,
    @Autowired private val studentRepository: StudentRepository,
    @Autowired private val applyHistoryRepository: ApplyHistoryRepository,
) {
    @BeforeEach
    fun setUp() {
        lectureRepository.save(
            Lecture(
                title = "Lecture",
                applyStartAt = LocalDateTime.now().minusDays(1),
                lectureAt = LocalDateTime.now().plusDays(5),
            ),
        )

        (1..50).forEach {
            studentRepository.save(Student("Student_$it"))
        }
    }

    @AfterTestExecution
    fun deleteAll() {
        lectureRepository.deleteAll()
        studentRepository.deleteAll()
        applyHistoryRepository.deleteAll()
    }

    @Test
    fun `수강 신청이 성공하면 정원이 1 증가한다`() {
        val applyRequest =
            LectureApplyServiceDto.Apply(
                studentId = 1L,
                lectureId = 1L,
            )

        val applyResult = lectureApplyService.apply(applyRequest)

        val lecture = lectureRepository.findById(1L)

        assertThat(applyResult.lectureTitle).isEqualTo("Lecture")
        assertThat(applyResult.result).isEqualTo(true)
        assertThat(lecture!!.currentEnrollmentCount).isEqualTo(1)
    }

    @Test
    fun `50명이 동시에 신청을 하는 경우 30명만 신청이 가능하다고 성공 30개 실패 20개의 이력이 쌓인다`() {
        val threadCount = 50
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)
        val lectureId = 1L
        val students = studentRepository.findAll()

        repeat(threadCount) {
            executorService.submit {
                try {
                    val applyRequest = LectureApplyServiceDto.Apply(students[it].id, lectureId)
                    lectureApplyService.apply(applyRequest)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
        executorService.shutdown()

        // then
        val result = lectureRepository.findById(1L)
        assertThat(result!!.currentEnrollmentCount).isEqualTo(30)

        val histories = applyHistoryRepository.findAll()
        assertThat(histories.size).isEqualTo(50)
        assertThat(histories.filter { it.applyStatus == ApplyStatus.COMPLETED }.size).isEqualTo(30)
        assertThat(histories.filter { it.applyStatus == ApplyStatus.FAILED }.size).isEqualTo(20)
    }
}
