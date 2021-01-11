package com.sabgil.bbuckkugi.model

import com.sabgil.bbuckkugi.exception.IllegalDataException

sealed class Data {

    object Accept : Data() {
        override val byteValue: ByteArray = byteArrayOf(0x00, 0x01)
    }

    object Reject : Data() {
        override val byteValue: ByteArray = byteArrayOf(0x00, 0x02)
    }

    class Message(messageType: Int) : Data() {
        override val byteValue: ByteArray = byteArrayOf(MESSAGE_PREFIX_BYTE, messageType.toByte())
    }

    class Progress(value: Int) : Data() {
        override val byteValue: ByteArray = byteArrayOf(PROGRESS_PREFIX_BYTE, value.toByte())
    }

    abstract val byteValue: ByteArray

    companion object {
        private const val MESSAGE_PREFIX_BYTE: Byte = 0x01
        private const val PROGRESS_PREFIX_BYTE: Byte = 0x02

        fun fromBytes(byteArray: ByteArray) =
            when {
                byteArray contentEquals Accept.byteValue -> Accept
                byteArray contentEquals Reject.byteValue -> Reject
                byteArray.size == 2 && byteArray[0] == MESSAGE_PREFIX_BYTE ->
                    Message(byteArray[1].toInt())
                byteArray.size == 2 && byteArray[0] == PROGRESS_PREFIX_BYTE ->
                    Progress(byteArray[1].toInt())
                else -> throw IllegalDataException()
            }
    }
}