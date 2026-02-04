package com.kindaboii.journal.features.entries.impl.data.database.datasource.remote

interface SyncManager {
    suspend fun startSync()
    suspend fun stopSync()
}
