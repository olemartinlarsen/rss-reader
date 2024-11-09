package rss.reader.services

import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import rss.reader.database.models.Users
import rss.reader.database.models.Users.select

@Serializable
data class User(val username: String, val passwordHash: String)
class UserService(database: Database) {
    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    suspend fun create(user: User): Int = dbQuery {
        Users.insert {
            it[username] = user.username
            it[passwordHash] = user.passwordHash
        }[Users.id]
    }

    suspend fun findByUsername(username: String): User? {
        return dbQuery {
            Users.selectAll().where { Users.username eq username }
                .map { User(it[Users.username], it[Users.passwordHash]) }
                .singleOrNull()
        }
    }
}
