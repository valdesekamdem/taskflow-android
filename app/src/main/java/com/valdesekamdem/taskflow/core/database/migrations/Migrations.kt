package com.valdesekamdem.taskflow.core.database.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE tasks_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                priority TEXT NOT NULL,
                category TEXT NOT NULL,
                due_date INTEGER,
                reminder INTEGER,
                completed_at INTEGER,
                created_at INTEGER NOT NULL,
                updated_at INTEGER,
                notes TEXT
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            INSERT INTO tasks_new
                SELECT id, title, description, priority, category, due_date, reminder, NULL, created_at, updated_at, notes
                FROM tasks
            """.trimIndent()
        )
        db.execSQL("DROP TABLE tasks")
        db.execSQL("ALTER TABLE tasks_new RENAME TO tasks")
    }
}
