package com.hhplus.lecture.domain

import com.hhplus.lecture.domain.dto.LectureApplyServiceDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LectureApplyService(
    private val lectureManager: LectureManager,
    private val studentManager: StudentManager,
    private val applyHistoryManager: ApplyHistoryManager,
) {
    @Transactional
    fun apply(applyDto: LectureApplyServiceDto.Apply): LectureApplyServiceDto.ApplyResult {
        // 학생을 찾는다. -> studentManager
        val student = studentManager.findById(applyDto.studentId)

        // 신청하려고 하는 강의가 available 한지 확인한다. -> lectureManager
        lectureManager.findAvailableById(applyDto.lectureId)

        // 학생이 강의를 이미 등록했는지 찾는다. (applyHistory 의 status 가 completed 인지 찾는다) -> applyHistoryManager

        // 강의가 이미 꽉차지 않았는지 확인한다. -> lectureManager 의 lecture 의 capacity 를 넘기고, applyHistoryManager 에서 계산

        // 선착순으로 강의 신청을 한다. -> 이 서비스의 핵심 로직

        // 강의신청 기록을 저장한다. (성공 or 실패 모두) -> applyHistoryManager

        return LectureApplyServiceDto.ApplyResult(result = true)
    }
}
