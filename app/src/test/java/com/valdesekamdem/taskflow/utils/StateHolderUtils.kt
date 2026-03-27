package com.valdesekamdem.taskflow.utils

import com.valdesekamdem.taskflow.core.presentation.StateHolder

suspend fun <S : Any, E : Any> StateHolder<S, E>.test(block: suspend StateHolder<S, E>.() -> Unit) {
    block()
}