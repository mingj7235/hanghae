package com.hhplus.lecture.common.exception.handler

import com.hhplus.lecture.common.exception.errors.LectureException
import com.hhplus.lecture.common.exception.errors.StudentException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class LectureApplyControllerAdvice : ResponseEntityExceptionHandler() {
    data class ErrorResponse(val code: Int, val message: String)

    @ExceptionHandler(StudentException::class)
    fun handleStudentException(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.message.orEmpty()),
            HttpStatus.BAD_REQUEST,
        )
    }

    @ExceptionHandler(LectureException::class)
    fun handleLectureException(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.message.orEmpty()),
            HttpStatus.BAD_REQUEST,
        )
    }
}
