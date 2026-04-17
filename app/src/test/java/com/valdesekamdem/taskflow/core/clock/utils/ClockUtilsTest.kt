package com.valdesekamdem.taskflow.core.clock.utils

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.ZoneId
import kotlin.time.Instant

class ClockUtilsTest {
    @get:Rule
    val defaultLocaleRule = DefaultLocaleRule()

    @Test
    fun `toMonthDay formats instant with explicit zone`() {
        val instant = Instant.parse("2026-01-01T00:00:00.00Z")

        assertEquals(
            "January 01",
            instant.toMonthDay(
                zoneId = ZoneId.of("UTC"),
            ),
        )
    }

    @Test
    fun `toMonthDay resolves calendar date based on provided zone`() {
        val instant = Instant.parse("2026-01-01T00:30:00.00Z")

        assertEquals(
            "January 01",
            instant.toMonthDay(
                zoneId = ZoneId.of("UTC"),
            ),
        )
        assertEquals(
            "December 31",
            instant.toMonthDay(
                zoneId = ZoneId.of("America/Toronto"),
            ),
        )
    }

    @Test
    fun `toMonthDayYear formats instant with day and full year`() {
        val instant = Instant.parse("2026-01-01T00:00:00.00Z")

        assertEquals(
            "January 1, 2026",
            instant.toMonthDayYear(zoneId = ZoneId.of("UTC")),
        )
    }

    @Test
    fun `fromUtcToInstant maps epoch millis to start of day in the given zone`() {
        // The UTC date of the epoch millis is January 15. Toronto (UTC-5 in January) starts
        // that calendar day 5 hours later in UTC terms.
        val epochMillis = Instant.parse("2026-01-15T00:00:00.00Z").toEpochMilliseconds()

        assertEquals(
            Instant.parse("2026-01-15T05:00:00.00Z"),
            epochMillis.fromUtcToInstant(ZoneId.of("America/Toronto")),
        )
    }
}
