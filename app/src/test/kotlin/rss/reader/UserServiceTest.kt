package rss.reader

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import rss.reader.database.initDatabase
import rss.reader.database.models.Users
import rss.reader.services.User
import rss.reader.services.UserService

class UserServiceTest {

    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        val database = initDatabase(useTestDb = true)
        userService = UserService(database)
    }

    @AfterEach
    fun cleanup() {
        transaction {
            Users.deleteAll()
            SchemaUtils.create(Users)
        }
    }

    @Test
    fun `test create and findByUsername`() = runBlocking {
        val user = User(username = "testUser", passwordHash = "hashedPassword123")
        userService.create(user)
        val retrievedUser = userService.findByUsername(user.username)

        assertNotNull(retrievedUser, "The retrieved user should not be null")
        assertEquals(user.username, retrievedUser?.username, "Usernames should match")
        assertEquals(user.passwordHash, retrievedUser?.passwordHash, "Password hashes should match")
    }

    @Test
    fun `test findByUsername with non-existent user`() = runBlocking {
        val retrievedUser = userService.findByUsername("nonExistentUser")
        assertNull(retrievedUser, "The retrieved user should be null for a non-existent username")
    }

    @Test
    fun `test create duplicate user`() = runBlocking {
        val user = User(username = "testUser", passwordHash = "hashedPassword123")
        userService.create(user)

        val exception = assertThrows<Exception> {
            runBlocking {
                userService.create(user)
            }
        }

        assertNotNull(exception, "An exception should be thrown when creating a duplicate user")
    }
}