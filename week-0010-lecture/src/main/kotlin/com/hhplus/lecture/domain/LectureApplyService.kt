package com.hhplus.lecture.domain

import com.hhplus.lecture.domain.dto.LectureApplyServiceDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LectureApplyService(
    private val lectureManager: LectureManager,
    private val studentManager: StudentManager,
    private val applyHistoryManager: ApplyHistoryManager,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun apply(applyDto: LectureApplyServiceDto.Apply): LectureApplyServiceDto.ApplyResult {
        try {
            // 학생을 찾는다. -> studentManager
            val student = studentManager.findById(applyDto.studentId)

            // 신청하려고 하는 강의가 available 한지 확인한다. -> lectureManager
            val availableLecture = lectureManager.findAvailableById(applyDto.lectureId)

            // 학생이 강의를 이미 등록했는지 확인한다.
            applyHistoryManager.checkApplyStatus(
                studentId = student.studentId,
                lectureId = availableLecture.lectureId,
            )

            // 선착순으로 강의 신청을 한다. -> 이 서비스의 핵심 로직

            // 강의신청 기록을 저장한다. (성공 or 실패 모두) -> applyHistoryManager
        } catch (e: Exception) {
            logger.info(e.message)
        }

        return LectureApplyServiceDto.ApplyResult(result = true)
    }
}
