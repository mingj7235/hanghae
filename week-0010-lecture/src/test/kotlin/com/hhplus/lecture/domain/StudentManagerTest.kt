package com.hhplus.lecture.domain

import com.hhplus.lecture.common.exception.errors.StudentException
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
        `when`(studentRepository.findById(NON_EXISTED_USER_ID)).thenReturn(null)

        val exception =
            assertThrows<StudentException.StudentNotfound> {
                studentManager.findById(NON_EXISTED_USER_ID)
            }

        assertThat(exception)
            .message().contains("Not found user. [id] = [$NON_EXISTED_USER_ID]")
    }

    companion object {
        const val NON_EXISTED_USER_ID = -1L
    }
}
