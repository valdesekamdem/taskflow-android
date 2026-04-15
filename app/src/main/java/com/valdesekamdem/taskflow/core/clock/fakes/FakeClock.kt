package com.valdesekamdem.taskflow.core.clock.fakes

import kotlin.time.Clock
import kotlin.time.Instant

class FakeClock(val now: Instant = Instant.parse("2026-01-01T10:00:00.00Z")) : Clock {
    override fun now(): Instant = now
}
