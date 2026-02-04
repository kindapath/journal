package com.kindaboii.journal.data.database

import app.cash.sqldelight.db.SqlDriver
import com.powersync.DatabaseDriverFactory as PowerSyncDatabaseDriverFactory
import com.powersync.PowerSyncDatabase
import com.powersync.integrations.sqldelight.PowerSyncDriver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

actual class DatabaseDriverFactory(
    private val context: android.content.Context
) : PowerSyncDatabaseProvider {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override val powerSyncDatabase: PowerSyncDatabase by lazy {
        PowerSyncDatabase(
            factory = PowerSyncDatabaseDriverFactory(context),
            schema = entriesPowerSyncSchema,
            dbFilename = DatabaseConstants.DATABASE_FILE_NAME,
            scope = scope,
        )
    }

    actual suspend fun createDriver(): SqlDriver =
        PowerSyncDriver(powerSyncDatabase, scope)
}
