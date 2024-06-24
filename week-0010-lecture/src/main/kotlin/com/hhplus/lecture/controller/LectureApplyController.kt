package com.hhplus.lecture.controller

import com.hhplus.lecture.controller.request.LectureApplyRequest
import com.hhplus.lecture.controller.response.LectureApplyResponse
import com.hhplus.lecture.domain.LectureApplyService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/lectures")
class LectureApplyController(
    private val lectureApplyService: LectureApplyService,
) {
    @PostMapping("/apply")
    fun apply(
        @RequestBody request: LectureApplyRequest.Apply,
    ): LectureApplyResponse.ApplyResult {
        return LectureApplyResponse.ApplyResult.of(
            lectureApplyService.apply(
                LectureApplyRequest.Apply.toApplyDto(request),
            ),
        )
    }
}
