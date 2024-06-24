package com.hhplus.lecture.domain

import com.hhplus.lecture.infra.repository.LectureRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

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
        lectureRepository.deleteAll()
    }

    @Test
    fun `존재하지 않는 강의를 조회하려고 하는 경우 예외를 리턴한다`() {
        TODO()
    }

    @Test
    fun `available_status 가 CLOSED 인 경우 예외를 리턴한다`() {
        TODO()
    }

    companion object {
        const val NON_EXISTED_LECTURE_ID = -1L
    }
}
