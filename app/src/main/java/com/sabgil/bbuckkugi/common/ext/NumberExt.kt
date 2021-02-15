package com.sabgil.bbuckkugi.common.ext

import java.nio.ByteBuffer

fun Long.toBytes(): ByteArray {
    val buffer: ByteBuffer = ByteBuffer.allocate(8)
    buffer.putLong(this)
    return buffer.array()
}

fun ByteArray.toLong(): Long {
    val buffer: ByteBuffer = ByteBuffer.allocate(8)
    buffer.put(this)
    buffer.flip()
    return buffer.long
}