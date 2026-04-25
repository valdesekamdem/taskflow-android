package com.valdesekamdem.taskflow.core.clock.utils

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant

fun Instant.toMonthDay(zoneId: ZoneId): String = this.toString("MMMM dd", zoneId)

fun Instant.toMonthDayYear(zoneId: ZoneId): String = this.toString("MMMM d, yyyy", zoneId)

fun Instant.toString(pattern: String, zoneId: ZoneId): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault()).withZone(zoneId)
    return formatter.format(this.toJavaInstant())
}

fun Instant.toRelativeDateText(now: Instant, zoneId: ZoneId): String {
    val today = now.toJavaInstant().atZone(zoneId).toLocalDate()
    val targetDay = this.toJavaInstant().atZone(zoneId).toLocalDate()
    return when (val days = ChronoUnit.DAYS.between(today, targetDay).toInt()) {
        -1 -> "Yesterday"
        0 -> "Today"
        1 -> "Tomorrow"
        in Int.MIN_VALUE..-2 -> "${-days} days ago"
        else -> "In $days days"
    }
}

fun Long.fromUtcToInstant(zoneId: ZoneId): Instant {
    return java.time.Instant.ofEpochMilli(this)
        .atZone(java.time.ZoneOffset.UTC)
        .toLocalDate()
        .atStartOfDay(zoneId)
        .toInstant()
        .toKotlinInstant()
}

fun Instant.atStartOfDay(zoneId: ZoneId): Instant {
    return this.toJavaInstant().atZone(zoneId)
        .toLocalDate()
        .atStartOfDay(zoneId)
        .toInstant()
        .toKotlinInstant()
}
