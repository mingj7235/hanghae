package io.hhplus.tdd.point.exception

object PointException {
    class InvalidChargePointAmountException(
        errorMessage: String,
    ) : RuntimeException(errorMessage)
}
