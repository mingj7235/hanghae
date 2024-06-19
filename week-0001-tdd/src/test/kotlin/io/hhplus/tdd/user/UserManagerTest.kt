package io.hhplus.tdd.user

import io.hhplus.tdd.database.UserRepository
import io.hhplus.tdd.point.UserManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
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
    fun `notExistUserTest`() {
        // Given
        val notExistUserId = -1L
        `given`(userRepository.findBy(notExistUserId)).willReturn(null)

        // When
        val existUser = userManager.existUser(notExistUserId)

        // Then
        assertThat(existUser).isEqualTo(false)
    }
}
