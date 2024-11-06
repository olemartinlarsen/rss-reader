package rss.reader

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
    fun `test create and read user`() = runBlocking {
        val user = User(username = "testUser", passwordHash = "hashedPassword123")

        val userId = userService.create(user)
        val retrievedUser = userService.read(userId)

        assertNotNull(retrievedUser)
        assertEquals(user.username, retrievedUser?.username)
        assertEquals(user.passwordHash, retrievedUser?.passwordHash)
    }
}