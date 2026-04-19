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
    fun `toRelativeDateText returns Yesterday when target is one day before now`() {
        val now = Instant.parse("2026-01-01T12:00:00.00Z")
        val target = Instant.parse("2025-12-31T12:00:00.00Z")

        assertEquals("Yesterday", target.toRelativeDateText(now, ZoneId.of("UTC")))
    }

    @Test
    fun `toRelativeDateText returns Today when target is same calendar day as now`() {
        val now = Instant.parse("2026-01-01T12:00:00.00Z")
        val target = Instant.parse("2026-01-01T06:00:00.00Z")

        assertEquals("Today", target.toRelativeDateText(now, ZoneId.of("UTC")))
    }

    @Test
    fun `toRelativeDateText returns Tomorrow when target is one day after now`() {
        val now = Instant.parse("2026-01-01T12:00:00.00Z")
        val target = Instant.parse("2026-01-02T12:00:00.00Z")

        assertEquals("Tomorrow", target.toRelativeDateText(now, ZoneId.of("UTC")))
    }

    @Test
    fun `toRelativeDateText returns X days ago when target is multiple days in the past`() {
        val now = Instant.parse("2026-01-10T12:00:00.00Z")
        val target = Instant.parse("2026-01-07T12:00:00.00Z")

        assertEquals("3 days ago", target.toRelativeDateText(now, ZoneId.of("UTC")))
    }

    @Test
    fun `toRelativeDateText returns In X days when target is multiple days in the future`() {
        val now = Instant.parse("2026-01-01T12:00:00.00Z")
        val target = Instant.parse("2026-01-06T12:00:00.00Z")

        assertEquals("In 5 days", target.toRelativeDateText(now, ZoneId.of("UTC")))
    }

    @Test
    fun `toRelativeDateText respects zone when determining calendar day`() {
        // 2026-01-01T01:00:00Z is still Dec 31 in Toronto (UTC-5)
        val now = Instant.parse("2026-01-01T12:00:00.00Z")
        val target = Instant.parse("2026-01-01T01:00:00.00Z")

        assertEquals("Today", target.toRelativeDateText(now, ZoneId.of("UTC")))
        assertEquals("Yesterday", target.toRelativeDateText(now, ZoneId.of("America/Toronto")))
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
