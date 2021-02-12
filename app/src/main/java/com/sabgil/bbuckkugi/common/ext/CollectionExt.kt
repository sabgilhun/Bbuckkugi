package com.sabgil.bbuckkugi.common.ext

public fun <T> Collection<T>?.toMutableList(): MutableList<T> =
    if (this == null) {
        ArrayList()
    } else {
        ArrayList(this)
    }