package com.valdesekamdem.taskflow.core.clock.utils

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Instant
import kotlin.time.toJavaInstant

fun Instant.toMonthDay(zoneId: ZoneId): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM dd", Locale.getDefault()).withZone(zoneId)
    return formatter.format(this.toJavaInstant())
}
