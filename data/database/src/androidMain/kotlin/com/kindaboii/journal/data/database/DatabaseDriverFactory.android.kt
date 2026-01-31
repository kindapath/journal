package com.kindaboii.journal.data.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory(private val context: android.content.Context) {
    actual suspend fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(schema = AppDatabase.Schema.synchronous(), context, DatabaseConstants.DATABASE_FILE_NAME)
    }
}
