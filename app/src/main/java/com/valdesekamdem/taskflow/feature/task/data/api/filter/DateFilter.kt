package com.valdesekamdem.taskflow.feature.task.data.api.filter

import kotlin.time.Instant

sealed interface DateFilter {
    data class Before(val date: Instant) : DateFilter

    data class After(val date: Instant) : DateFilter

    data class Between(val start: Instant, val end: Instant) : DateFilter
}