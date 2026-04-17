package com.valdesekamdem.taskflow.feature.utils

import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Result.failure(e)
    }
}