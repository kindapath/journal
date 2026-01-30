package com.kindaboii.journal.features.entries.impl

object Constants {
    const val ENTRY_DB = "entry.db"
    const val SQL_JS_WORKER_URL =
        """new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)"""
    const val DATABASE_NOT_INITIALIZED = "Database is not initialized"
}