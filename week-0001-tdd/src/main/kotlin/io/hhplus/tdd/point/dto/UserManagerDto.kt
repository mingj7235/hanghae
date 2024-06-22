package io.hhplus.tdd.point.dto

object UserManagerDto {
    data class User(
        val userId: Long,
    ) {
        companion object {
            fun of(userData: io.hhplus.tdd.user.data.User): User {
                return User(
                    userId = userData.id,
                )
            }
        }
    }
}
