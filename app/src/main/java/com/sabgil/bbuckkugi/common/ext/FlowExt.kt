package com.sabgil.bbuckkugi.common.ext

import com.sabgil.bbuckkugi.common.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
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


fun <E> SendChannel<Data<E>>.offerSuccess(element: E) = offer(Data.Success.of(element))

fun <E> SendChannel<Data<E>>.offerEmptySuccess() = offer(Data.Success.emptyOf())

fun <E> SendChannel<Data<E>>.offerFailure(throwable: Throwable) = offer(Data.Failure(throwable))