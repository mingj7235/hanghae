package com.hhplus.lecture.domain

import com.hhplus.lecture.common.exception.errors.StudentException
import com.hhplus.lecture.infra.entity.Student
import com.hhplus.lecture.infra.repository.StudentRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

/**
 * StudentManager 단위 테스트
 */
class StudentManagerTest {
    @Mock
    private lateinit var studentRepository: StudentRepository

    @InjectMocks
    private lateinit var studentManager: StudentManager

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        studentRepository.deleteAll()
    }

    @Test
    fun `존재하지 않는 학생을 조회할 경우 예외를 리턴한다`() {
        `when`(studentRepository.findById(NON_EXISTED_STUDENT_ID)).thenReturn(null)

        val exception =
            assertThrows<StudentException.StudentNotfound> {
                studentManager.findById(NON_EXISTED_STUDENT_ID)
            }

        assertThat(exception)
            .message()
            .contains("Not found user")
    }

    @Test
    fun `존재하는 학생이라면 그 학생을 리턴한다`() {
        val studentId = 0L
        val studentName = "Student"
        `when`(studentRepository.findById(studentId)).thenReturn(
            Student(
                name = studentName,
            ),
        )

        val student = studentManager.findById(studentId)

        assertThat(student.id).isEqualTo(studentId)
        assertThat(student.name).isEqualTo(studentName)
    }

    companion object {
        const val NON_EXISTED_STUDENT_ID = -1L
    }
}
