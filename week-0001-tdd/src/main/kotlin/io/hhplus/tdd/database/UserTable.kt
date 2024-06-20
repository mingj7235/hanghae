package io.hhplus.tdd.database

import io.hhplus.tdd.user.data.User
import org.springframework.stereotype.Component

@Component
class UserTable : UserRepository {
    private val table = HashMap<Long, User>()

    override fun findBy(id: Long): User? {
        return table[id]
    }

    override fun save(id: Long): User {
        val user = User(id)
        table[id] = user
        return user
    }
}
