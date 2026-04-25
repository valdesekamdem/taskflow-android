package com.valdesekamdem.taskflow.feature.task.data.real

import com.valdesekamdem.taskflow.feature.task.data.api.filter.DateFilter
import com.valdesekamdem.taskflow.feature.task.data.api.filter.TaskFilter
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Instant

class TaskQueryBuilderTest {

    @Test
    fun `toQuery returns base SELECT when no filters are set`() {
        assertEquals("SELECT * FROM tasks", TaskFilter().toQuery())
    }

    @Test
    fun `toQuery adds completed_at IS NOT NULL when isCompleted is true`() {
        assertEquals(
            "SELECT * FROM tasks WHERE completed_at IS NOT NULL",
            TaskFilter(isCompleted = true).toQuery(),
        )
    }

    @Test
    fun `toQuery adds completed_at IS NULL when isCompleted is false`() {
        assertEquals(
            "SELECT * FROM tasks WHERE completed_at IS NULL",
            TaskFilter(isCompleted = false).toQuery(),
        )
    }

    @Test
    fun `toQuery adds Before condition on due_date`() {
        val date = Instant.parse("2026-01-15T00:00:00.00Z")

        assertEquals(
            "SELECT * FROM tasks WHERE due_date < ${date.toEpochMilliseconds()}",
            TaskFilter(dueDate = DateFilter.Before(date)).toQuery(),
        )
    }

    @Test
    fun `toQuery adds After condition on due_date`() {
        val date = Instant.parse("2026-01-15T00:00:00.00Z")

        assertEquals(
            "SELECT * FROM tasks WHERE due_date > ${date.toEpochMilliseconds()}",
            TaskFilter(dueDate = DateFilter.After(date)).toQuery(),
        )
    }

    @Test
    fun `toQuery adds BETWEEN condition on due_date`() {
        val start = Instant.parse("2026-01-01T00:00:00.00Z")
        val end = Instant.parse("2026-01-31T00:00:00.00Z")

        assertEquals(
            "SELECT * FROM tasks WHERE due_date BETWEEN ${start.toEpochMilliseconds()} AND ${end.toEpochMilliseconds()}",
            TaskFilter(dueDate = DateFilter.Between(start, end)).toQuery(),
        )
    }

    @Test
    fun `toQuery joins dueDate and isCompleted with AND`() {
        val date = Instant.parse("2026-01-15T00:00:00.00Z")

        assertEquals(
            "SELECT * FROM tasks WHERE due_date < ${date.toEpochMilliseconds()} AND completed_at IS NOT NULL",
            TaskFilter(dueDate = DateFilter.Before(date), isCompleted = true).toQuery(),
        )
    }
}
