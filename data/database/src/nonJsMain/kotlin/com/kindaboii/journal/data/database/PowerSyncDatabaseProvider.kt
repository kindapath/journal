package com.kindaboii.journal.data.database

import com.powersync.PowerSyncDatabase

interface PowerSyncDatabaseProvider {
    val powerSyncDatabase: PowerSyncDatabase
}
