package com.kindaboii.journal.data.database

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    suspend fun createDriver(): SqlDriver
}
