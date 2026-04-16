package com.valdesekamdem.taskflow.core.clock.utils

import java.time.ZoneId
import java.time.format.DateTimeFormatter
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

fun Long.fromUtcToInstant(zoneId: ZoneId): Instant {
    return java.time.Instant.ofEpochMilli(this)
        .atZone(java.time.ZoneOffset.UTC)
        .toLocalDate()
        .atStartOfDay(zoneId)
        .toInstant()
        .toKotlinInstant()
}
