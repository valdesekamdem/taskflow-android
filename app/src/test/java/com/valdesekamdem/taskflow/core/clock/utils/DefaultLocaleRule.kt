package com.valdesekamdem.taskflow.core.clock.utils

import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.util.Locale

class DefaultLocaleRule(
    private val locale: Locale = Locale.CANADA,
) : TestWatcher() {
    private lateinit var previousLocale: Locale

    override fun starting(description: Description) {
        previousLocale = Locale.getDefault()
        Locale.setDefault(locale)
    }

    override fun finished(description: Description) {
        Locale.setDefault(previousLocale)
    }
}
