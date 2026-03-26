package com.valdesekamdem.taskflow.core.presentation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey

interface UiFactory {
    fun register(scope: EntryProviderScope<NavKey>)
}
