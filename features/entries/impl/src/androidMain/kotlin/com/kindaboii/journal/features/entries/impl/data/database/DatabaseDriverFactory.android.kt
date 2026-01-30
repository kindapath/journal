package com.kindaboii.journal.features.entries.impl.data.database

import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory(private val context: android.content.Context) {
    actual suspend fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(schema = EntryDatabase.Schema.synchronous(), context, "entries.db")
    }
}