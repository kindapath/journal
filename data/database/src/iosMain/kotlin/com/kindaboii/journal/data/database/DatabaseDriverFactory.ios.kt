package com.kindaboii.journal.data.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver


actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema.synchronous(), DatabaseConstants.DATABASE_FILE_NAME)
    }
}
