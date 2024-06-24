package com.hhplus.lecture.domain

import com.hhplus.lecture.infra.repository.ApplyHistoryRepository
import com.hhplus.lecture.infra.repository.LectureRepository
import com.hhplus.lecture.infra.repository.StudentRepository
import org.junit.jupiter.api.BeforeEach
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations

/**
 * LectureApplyService 단위 테스트
 */
class LectureApplyServiceTest {
    @Mock
    private lateinit var lectureRepository: LectureRepository

    @Mock
    private lateinit var studentRepository: StudentRepository

    @Mock
    private lateinit var applyHistoryRepository: ApplyHistoryRepository

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

    companion object {
        const val NON_EXISTED_USER_ID = -1L
    }
}
