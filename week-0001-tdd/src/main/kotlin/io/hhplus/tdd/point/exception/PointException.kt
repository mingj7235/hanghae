package io.hhplus.tdd.point.exception

object PointException {
    class InvalidAmountException(
        errorMessage: String,
    ) : RuntimeException(errorMessage)

    class InsufficientPointsException(
        errorMessage: String,
    ) : RuntimeException(errorMessage)
}
