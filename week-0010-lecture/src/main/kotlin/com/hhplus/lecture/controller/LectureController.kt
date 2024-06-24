package com.hhplus.lecture.controller

import com.hhplus.lecture.controller.request.LectureRequest
import com.hhplus.lecture.controller.response.LectureResponse
import com.hhplus.lecture.domain.lecture.LectureService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/lectures")
class LectureController(
    private val lectureService: LectureService,
) {
    @PostMapping("/apply")
    fun apply(
        @RequestBody request: LectureRequest.Apply,
    ): LectureResponse.ApplyResult {
        return LectureResponse.ApplyResult(result = true)
    }
}
