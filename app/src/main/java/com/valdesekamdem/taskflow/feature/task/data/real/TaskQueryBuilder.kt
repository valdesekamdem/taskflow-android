package com.valdesekamdem.taskflow.feature.task.data.real

import com.valdesekamdem.taskflow.feature.task.data.api.filter.DateFilter
import com.valdesekamdem.taskflow.feature.task.data.api.filter.TaskFilter

fun TaskFilter.toQuery(): String {
    val conditions = mutableListOf<String>()

    if (dueDate != null) {
        conditions.add(dueDate.toQueryCondition("due_date"))
    }

    if (isCompleted != null) {
        conditions.add("completed_at IS ${if (isCompleted) "NOT NULL" else "NULL"}")
    }

    val query = StringBuilder("SELECT * FROM tasks")
    if (conditions.isNotEmpty()) {
        query.append(" WHERE ")
        query.append(conditions.joinToString(" AND "))
    }

    return query.toString()
}

private fun DateFilter.toQueryCondition(field: String): String {
    return when (this) {
        is DateFilter.After -> "$field > ${date.toEpochMilliseconds()}"
        is DateFilter.Before -> "$field < ${date.toEpochMilliseconds()}"
        is DateFilter.Between -> "$field BETWEEN ${start.toEpochMilliseconds()} AND ${end.toEpochMilliseconds()}"
    }
}
