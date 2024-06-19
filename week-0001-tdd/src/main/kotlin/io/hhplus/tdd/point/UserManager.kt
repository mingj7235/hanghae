package io.hhplus.tdd.point

import io.hhplus.tdd.database.UserRepository
import io.hhplus.tdd.point.dto.UserManagerDto
import org.springframework.stereotype.Component

@Component
class UserManager(
    private val userRepository: UserRepository,
) {
    fun existUser(userId: Long): Boolean = userRepository.findBy(userId) != null

    fun register(userId: Long): UserManagerDto.User {
        return UserManagerDto.User.of(
            userRepository.save(userId),
        )
    }
}
