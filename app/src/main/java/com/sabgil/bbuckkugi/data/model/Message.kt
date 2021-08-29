package com.sabgil.bbuckkugi.data.model

import com.sabgil.bbuckkugi.common.ext.toBytes
import com.sabgil.bbuckkugi.common.ext.toLong
import com.sabgil.bbuckkugi.data.entities.MessageEntity
import com.sabgil.bbuckkugi.data.model.enums.Gender
import com.sabgil.bbuckkugi.data.model.enums.LoginWay
import java.io.Serializable

sealed class Message : Serializable {

    abstract val byteValue: ByteArray

    object Start : Message() {

        override val byteValue: ByteArray = byteArrayOf(START_PREFIX_BYTE)
    }

    class Reply(
        val messageType: Int,
        val loginWay: LoginWay,
        val isAgreed: Boolean,
        val id: String,
        val name: String,
        val gender: Gender,
        val time: Long
    ) : Message() {

        override val byteValue: ByteArray = buildByteArray()

        private fun buildByteArray(): ByteArray {
            val bytes = ByteArray(REPLY_MESSAGE_SIZE) { UTF_8_BLANK_BYTE }

            bytes[0] = REPLY_PREFIX_BYTE

            bytes[1] = messageType.toByte()

            bytes[2] = if (loginWay == LoginWay.KAKAO) 0x00 else 0x01

            bytes[3] = if (isAgreed) 0x01 else 0x00

            val idBytes = id.toByteArray()
            if (idBytes.size > 30) throw IllegalArgumentException()
            idBytes.copyInto(bytes, 4)

            val nameBytes = name.toByteArray()
            if (nameBytes.size > 30) throw IllegalArgumentException()
            nameBytes.copyInto(bytes, 34)

            bytes[64] = if (gender == Gender.MALE) 0x00 else 0x01

            val timeBytes = time.toBytes()
            if (timeBytes.size > 8) throw IllegalArgumentException()
            timeBytes.copyInto(bytes, 65)

            return bytes
        }

        companion object {
            fun from(from: MessageEntity) = Reply(
                from.messageType,
                from.loginWay,
                from.isAgreed,
                from.userId,
                from.name,
                from.gender,
                from.time
            )
        }
    }

    class Send(messageType: Int) : Message() {

        override val byteValue: ByteArray = byteArrayOf(MESSAGE_PREFIX_BYTE, messageType.toByte())

        val messageCardIndex = byteValue[1].toInt()
    }

    companion object {

        private const val START_PREFIX_BYTE: Byte = 0x00
        private const val REPLY_PREFIX_BYTE: Byte = 0x01
        private const val MESSAGE_PREFIX_BYTE: Byte = 0x02

        private const val UTF_8_BLANK_BYTE: Byte = 0x20
        private const val REPLY_MESSAGE_SIZE = 73

        fun fromBytes(byteArray: ByteArray) =
            when (byteArray[0]) {
                MESSAGE_PREFIX_BYTE -> parseSend(byteArray)
                REPLY_PREFIX_BYTE -> parseReply(byteArray)
                else -> throw IllegalArgumentException()
            }

        private fun parseSend(byteArray: ByteArray) = Send(byteArray[1].toInt())

        private fun parseReply(byteArray: ByteArray) = Reply(
            byteArray[1].toInt(),
            if (byteArray[2] == 0x00.toByte()) LoginWay.KAKAO else LoginWay.NAVER,
            byteArray[3] != 0x00.toByte(),
            String(byteArray.copyOfRange(4, 34)).trim(),
            String(byteArray.copyOfRange(34, 64)).trim(),
            if (byteArray[64] == 0x00.toByte()) Gender.MALE else Gender.FEMALE,
            byteArray.copyOfRange(65, 73).toLong()
        )
    }
}