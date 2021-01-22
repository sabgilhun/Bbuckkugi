package com.sabgil.bbuckkugi.common.ext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

public suspend inline fun <T> Flow<T>.collectOnMain(crossinline action: suspend (value: T) -> Unit): Unit =
    collect {
        withContext(Dispatchers.Main) {
            action(it)
        }
    }
