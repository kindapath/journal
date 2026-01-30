package com.kindaboii.journal.features.entries.impl.data.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        val appFolderName = "JournalApp"
        val dbName = "entries.db"

        val documentsFolder = File(System.getProperty("user.home"), "Documents")
        val appFolder = File(documentsFolder, appFolderName)

        if (!appFolder.exists()) {
            appFolder.mkdirs()
        }

        val databaseFile = File(appFolder, dbName)
        val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${databaseFile.absolutePath}")

        if (!databaseFile.exists() || databaseFile.length() == 0L) {
            EntryDatabase.Schema.create(driver).await()
        }

        return driver
    }
}
