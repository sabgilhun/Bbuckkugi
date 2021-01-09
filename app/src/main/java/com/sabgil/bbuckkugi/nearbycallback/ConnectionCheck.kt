package com.sabgil.bbuckkugi.nearbycallback

sealed class ConnectionCheck {

    abstract fun toBytes(): ByteArray

    object Accept : ConnectionCheck() {
        override fun toBytes() = acceptByteValue
    }

    object Reject : ConnectionCheck() {
        override fun toBytes() = rejectByteValue
    }

    companion object {
        private val acceptByteValue = byteArrayOf(0x00, 0x00)
        private val rejectByteValue = byteArrayOf(0x00, 0x01)

        fun fromBytes(bytes: ByteArray) =
            when {
                bytes contentEquals acceptByteValue -> Accept
                bytes contentEquals rejectByteValue -> Reject
                else -> throw IllegalArgumentException()
            }
    }
}