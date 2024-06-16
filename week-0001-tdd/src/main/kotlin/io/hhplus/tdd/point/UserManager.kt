package io.hhplus.tdd.point

import io.hhplus.tdd.database.UserRepository
import org.springframework.stereotype.Component

@Component
class UserManager(
    private val userRepository: UserRepository,
) {
    fun existUser(userId: Long): Boolean = userRepository.findBy(userId) != null
}