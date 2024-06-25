package com.hhplus.lecture.common.exception.errors

open class StudentException(
    errorMessage: String,
) : RuntimeException(errorMessage) {
    class StudentNotfound(
        errorMessage: String,
    ) : StudentException(errorMessage)
}
