package com.sabgil.bbuckkugi.common.ext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

suspend inline fun <T> Flow<T>.collectOnMain(
    crossinline action: suspend (value: T) -> Unit
) = collect {
    withContext(Dispatchers.Main) {
        action(it)
    }
}
