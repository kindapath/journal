package com.kindaboii.journal.lifecycle

import com.kindaboii.journal.data.database.sync.SyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Manages app-level lifecycle events like initialization and cleanup.
 * Responsible for starting/stopping sync and other app-wide services.
 */
class AppLifecycleManager(
    private val syncManager: SyncManager
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * Called when the app starts.
     * Initializes sync and other app-wide services.
     */
    fun onAppStart() {
        scope.launch {
            syncManager.startSync()
        }
    }

    /**
     * Called when the app stops.
     * Cleans up sync and other app-wide services.
     */
    fun onAppStop() {
        scope.launch {
            syncManager.stopSync()
        }
    }
}
