package com.hhplus.lecture.common.exception.errors

open class LectureException(
    errorMessage: String,
) : RuntimeException(errorMessage) {
    class LectureNotfound : LectureException("Not found lecture")

    class InvalidLectureApplyDateTime : LectureException("Invalid lecture apply date time")
}
