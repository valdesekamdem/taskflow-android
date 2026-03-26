package com.valdesekamdem.taskflow.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import com.valdesekamdem.taskflow.core.presentation.StateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class HomeViewModel @Inject constructor(): ViewModel(), StateHolder<Unit, Unit> {
    override val uiState: StateFlow<Unit> = MutableStateFlow(Unit)

    override fun onUiEvent(event: Unit) {}
}