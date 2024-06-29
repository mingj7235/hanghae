package com.hhplus.lecture.controller

import com.hhplus.lecture.application.LectureApplyService
import com.hhplus.lecture.controller.request.LectureApplyRequest
import com.hhplus.lecture.controller.response.LectureApplyResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
    ): LectureApplyResponse.ApplyResult =
        LectureApplyResponse.ApplyResult.of(
            lectureApplyService.apply(
                LectureApplyRequest.Apply.toApplyDto(request),
            ),
        )

    @GetMapping
    fun getLectures(): List<LectureApplyResponse.Lecture> =
        lectureApplyService
            .getLectures()
            .map { LectureApplyResponse.Lecture.of(it) }

    @GetMapping("/{lectureId}/application/{studentId}")
    fun getApplyStatus(
        @PathVariable lectureId: Long,
        @PathVariable studentId: Long,
    ): LectureApplyResponse.Status =
        LectureApplyResponse.Status.of(
            lectureApplyService.getApplyStatus(
                lectureId = lectureId,
                studentId = studentId,
            ),
        )
}
