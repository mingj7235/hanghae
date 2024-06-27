package com.hhplus.lecture.common.exception.errors

open class StudentException(
    errorMessage: String,
) : RuntimeException(errorMessage) {
    class StudentNotfound : StudentException("Not found student")
}
