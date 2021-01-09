package com.sabgil.bbuckkugi.common

sealed class Result<T> {

    data class Success<T>(val result: T) : Result<T>()

    object Loading : Result<Nothing>()

    data class Error<T : Exception>(val exception: T) : Result<T>()
}