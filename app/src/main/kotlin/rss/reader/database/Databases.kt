package rss.reader.database

import org.jetbrains.exposed.sql.Database

fun initDatabase(useTestDb: Boolean = false): Database {
    val dbUrl = if (useTestDb) "jdbc:sqlite:test.db" else "jdbc:sqlite:data.db"
    return Database.connect(dbUrl, driver = "org.sqlite.JDBC")
}