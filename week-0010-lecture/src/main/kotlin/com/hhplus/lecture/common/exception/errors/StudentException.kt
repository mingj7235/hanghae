package com.hhplus.lecture.common.exception.errors

open class StudentException(
    errorMessage: String,
) : RuntimeException(errorMessage) {
    inner class StudentNotfound(errorMessage: String) : StudentException(errorMessage)
}
