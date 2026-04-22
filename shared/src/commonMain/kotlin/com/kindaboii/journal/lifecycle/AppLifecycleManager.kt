package com.kindaboii.journal.lifecycle

import com.kindaboii.journal.domain.AuthService
import com.kindaboii.journal.domain.AuthState
import com.kindaboii.journal.data.database.sync.SyncManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Manages app-level lifecycle events like initialization and cleanup.
 * Responsible for starting/stopping sync and other app-wide services.
 */
class AppLifecycleManager(
    private val syncManager: SyncManager,
    private val authService: AuthService,
    private val demoSessionManager: DemoSessionManager,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var authObserverJob: Job? = null
    private var isSyncRunning = false
    private var currentSyncUserId: String? = null

    /**
     * Called when the app starts.
     * Initializes sync and other app-wide services.
     */
    fun onAppStart() {
        if (authObserverJob != null) return
        authObserverJob = scope.launch {
            authService.authState.collect { authState ->
                when (authState) {
                    is AuthState.Authenticated -> {
                        if (!isSyncRunning) {
                            syncManager.startSync()
                            isSyncRunning = true
                            currentSyncUserId = authState.userId
                        } else if (currentSyncUserId != authState.userId) {
                            syncManager.stopSync()
                            syncManager.startSync()
                            currentSyncUserId = authState.userId
                        }

                        demoSessionManager.ensureDemoData(authState.userId)
                    }

                    AuthState.Loading,
                    AuthState.Unauthenticated,
                    -> {
                        if (isSyncRunning) {
                            syncManager.stopSync()
                            isSyncRunning = false
                        }
                        currentSyncUserId = null
                    }
                }
            }
        }
    }

    /**
     * Called when the app stops.
     * Cleans up sync and other app-wide services.
     */
    fun onAppStop() {
        authObserverJob?.cancel()
        authObserverJob = null
        scope.launch {
            if (isSyncRunning) {
                syncManager.stopSync()
                isSyncRunning = false
            }
            currentSyncUserId = null
        }
    }
}
