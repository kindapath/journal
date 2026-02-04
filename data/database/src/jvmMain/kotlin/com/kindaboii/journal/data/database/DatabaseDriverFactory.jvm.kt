package com.kindaboii.journal.data.database

import app.cash.sqldelight.db.SqlDriver
import com.powersync.DatabaseDriverFactory as PowerSyncDatabaseDriverFactory
import com.powersync.PowerSyncDatabase
import com.powersync.integrations.sqldelight.PowerSyncDriver
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

actual class DatabaseDriverFactory : PowerSyncDatabaseProvider {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override val powerSyncDatabase: PowerSyncDatabase by lazy {
        val appFolderName = "JournalApp"
        val dbName = DatabaseConstants.DATABASE_FILE_NAME

        val documentsFolder = File(System.getProperty("user.home"), "Documents")
        val appFolder = File(documentsFolder, appFolderName)

        if (!appFolder.exists()) {
            appFolder.mkdirs()
        }

        PowerSyncDatabase(
            factory = PowerSyncDatabaseDriverFactory(),
            schema = entriesPowerSyncSchema,
            dbFilename = dbName,
            scope = scope,
            dbDirectory = appFolder.absolutePath + File.separator,
        )
    }

    actual suspend fun createDriver(): SqlDriver =
        PowerSyncDriver(powerSyncDatabase, scope)
}
