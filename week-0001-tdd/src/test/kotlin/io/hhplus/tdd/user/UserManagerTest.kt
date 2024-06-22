package io.hhplus.tdd.user

import io.hhplus.tdd.database.UserRepository
import io.hhplus.tdd.point.UserManager
import io.hhplus.tdd.user.data.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class UserManagerTest {
    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userManager: UserManager

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `존재하지 않은 userId 를 조회했을 때, false 를 리턴한다`() {
        val notExistUserId = -1L

        `when`(userRepository.findBy(notExistUserId)).thenReturn(null)
        val existUser = userManager.existUser(notExistUserId)

        assertThat(existUser).isEqualTo(false)
    }

    @Test
    fun `존재하는 userId 를 조회했을 때, true 를 리턴한다`() {
        val existUserId = 0L
        val user = User(existUserId)

        `when`(userRepository.findBy(existUserId)).thenReturn(user)
        val existUser = userManager.existUser(existUserId)

        assertThat(existUser).isEqualTo(true)
    }
}
