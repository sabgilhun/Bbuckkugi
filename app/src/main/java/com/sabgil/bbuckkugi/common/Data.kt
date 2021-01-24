package com.sabgil.bbuckkugi.common

import java.io.Serializable

sealed class Data<out T> : Serializable {

    abstract class Success<T> : Data<T>() {
        abstract val data: T

        companion object {
            fun <E> of(element: E): Success<E> = Exist(element)

            fun emptyOf(): Success<Nothing> = Empty
        }
    }

    private data class Exist<T>(private val _data: T) : Success<T>(), Serializable {
        override val data: T
            get() = _data
    }

    private object Empty : Success<Nothing>() {
        override val data: Nothing
            get() = throw IllegalAccessException()
    }

    data class Failure(val exception: Throwable) : Data<Nothing>(), Serializable
}