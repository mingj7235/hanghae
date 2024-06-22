package io.hhplus.tdd.user.exception

object UserException {
    class UserNotFound(
        errorMessage: String
    ) : RuntimeException(errorMessage)
}