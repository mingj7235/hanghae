package com.hhplus.lecture.common.exception.errors

open class ApplyHistoryException(
    errorMessage: String,
) : RuntimeException(errorMessage) {
    class AlreadyApplied : ApplyHistoryException("Already applied")
}
