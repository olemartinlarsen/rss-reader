package rss.reader.services

import org.mindrot.jbcrypt.BCrypt

class AuthService(private val userService: UserService) {

    suspend fun registerUser(username: String, password: String): Boolean {
        val existingUser = userService.findByUsername(username)
        if (existingUser != null) {
            return false
        }

        val passwordHash = hashPassword(password)
        userService.create(User(username, passwordHash))
        return true
    }

    suspend fun authenticateUser(username: String, password: String): Boolean {
        val user = userService.findByUsername(username)
        return if (user != null) {
            verifyPassword(password, user.passwordHash)
        } else {
            false
        }
    }

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    private fun verifyPassword(plainPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(plainPassword, hashedPassword)
    }
}