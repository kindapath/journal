package com.kindaboii.journal.data.database

import com.powersync.db.schema.Column
import com.powersync.db.schema.Schema
import com.powersync.db.schema.Table

internal const val ENTRIES_TABLE = "entries"

internal val entriesPowerSyncSchema: Schema =
    Schema(
        listOf(
            Table(
                ENTRIES_TABLE,
                listOf(
                    Column.text("title"),
                    Column.text("body"),
                    Column.integer("mood_value"),
                    Column.text("mood_emotions"),
                    Column.text("mood_influences"),
                    Column.text("created_at"),
                    Column.text("updated_at"),
                    Column.text("deleted_at"),
                ),
            ),
        ),
    )
