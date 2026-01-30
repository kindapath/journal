package com.kindaboii.journal.features.entries.impl.data.database

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.w3c.dom.Worker


@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("""
() => new Worker(new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url))
""")
private external fun createSqlJsWorker(): Worker

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(): SqlDriver {
        val worker = createSqlJsWorker()
        val driver = WebWorkerDriver(worker)
        EntryDatabase.Schema.awaitCreate(driver)
        return driver
    }
}