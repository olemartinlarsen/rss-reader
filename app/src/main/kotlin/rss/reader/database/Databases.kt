package rss.reader.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import rss.reader.database.models.Users

fun initDatabase() {
    Database.connect("jdbc:sqlite:data.db", driver = "org.sqlite.JDBC")
    transaction {
        SchemaUtils.create(Users)
    }
}