package com.valdesekamdem.taskflow.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * A screen binder that wires the state holder and the UI composable. This allows the composables
 * to be dumb (just render data), and not have any knowledge about the state holder.
 */
@Composable
fun <UiState, UiEvent, UiStateHolder: StateHolder<UiState, UiEvent>> BindScreen(
    stateHolder: UiStateHolder,
    component: @Composable (UiState, onUiEvent: (UiEvent) -> Unit) -> Unit,
) {
    val uiState by stateHolder.uiState.collectAsStateWithLifecycle()
    val onUiEvent = remember(stateHolder) {
        { event: UiEvent -> stateHolder.onUiEvent(event) }
    }

    component(uiState, onUiEvent)
}