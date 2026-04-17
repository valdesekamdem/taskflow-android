package com.valdesekamdem.taskflow.utils

import app.cash.turbine.TurbineTestContext
import com.valdesekamdem.taskflow.core.presentation.StateHolder

suspend fun <S : Any, E : Any> StateHolder<S, E>.test(block: suspend StateHolder<S, E>.() -> Unit) {
    block()
}

suspend fun <T> TurbineTestContext<T>.skipItem(message: String) {
    awaitItem()
}
