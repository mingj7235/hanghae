package io.hhplus.tdd.database

import io.hhplus.tdd.user.data.User

interface UserRepository {
    fun findBy(id: Long): User?

    fun save(id: Long): User
}
