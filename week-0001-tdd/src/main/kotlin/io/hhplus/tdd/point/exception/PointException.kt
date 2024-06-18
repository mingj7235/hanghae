package io.hhplus.tdd.point.exception

object PointException {
    class InvalidChargePointAmountException(
        errorMessage: String,
    ) : RuntimeException(errorMessage)

    class InsufficientPointsException(
        errorMessage: String,
    ) : RuntimeException(errorMessage)
}
