package com.sabgil.bbuckkugi.common

sealed class Result<out T> {

    data class Success<T>(val result: T) : Result<T>()

    data class Failure(val exception: Throwable) : Result<Nothing>()

    object Loading : Result<Nothing>()
}