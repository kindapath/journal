package com.kindaboii.journal.features.entries.impl.data.database.datasource.remote

class NoOpSyncManager : SyncManager {
    override suspend fun startSync() = Unit
    override suspend fun stopSync() = Unit
}
