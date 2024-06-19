package io.hhplus.tdd.point

import io.hhplus.tdd.point.exception.PointException
import io.hhplus.tdd.point.response.PointResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/point")
class PointController(
    private val pointService: PointService,
) {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    fun point(
        @PathVariable id: Long,
    ): PointResponse.Point {
        return PointResponse.Point.of(
            pointService.getPointBy(id),
        )
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{userId}/histories")
    fun history(
        @PathVariable userId: Long,
    ): PointResponse.History {
        return PointResponse.History.of(
            pointService.getHistoryBy(userId),
        )
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/charge")
    fun charge(
        @PathVariable id: Long,
        @RequestBody amount: Long,
    ): PointResponse.Point {
        if (amount < 0) {
            throw PointException.InvalidChargePointAmountException("Invalid Charge Point : [$amount]")
        }

        return PointResponse.Point.of(
            pointService.charge(id, amount),
        )
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    fun use(
        @PathVariable id: Long,
        @RequestBody amount: Long,
    ): PointResponse.Point {
        return PointResponse.Point.of(
            pointService.use(id, amount),
        )
    }
}
