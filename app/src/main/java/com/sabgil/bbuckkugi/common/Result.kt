package com.sabgil.bbuckkugi.common

import java.io.Serializable

sealed class Result<out T>: Serializable{

    data class Success<T>(val result: T) : Result<T>(), Serializable

    data class Failure(val exception: Throwable) : Result<Nothing>(), Serializable
}