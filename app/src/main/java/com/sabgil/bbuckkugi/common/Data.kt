package com.sabgil.bbuckkugi.common

import java.io.Serializable

sealed class Data<out T>: Serializable{

    data class Success<T>(val result: T) : Data<T>(), Serializable

    data class Failure(val exception: Throwable) : Data<Nothing>(), Serializable
}