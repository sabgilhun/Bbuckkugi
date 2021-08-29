package com.sabgil.bbuckkugi.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sabgil.bbuckkugi.data.model.Message
import com.sabgil.bbuckkugi.data.model.enums.Gender
import com.sabgil.bbuckkugi.data.model.enums.LoginWay

@Entity(tableName = "MESSAGE")
data class MessageEntity constructor(

    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @ColumnInfo(name = "message_type")
    val messageType: Int,

    @ColumnInfo(name = "loginWay")
    val loginWay: LoginWay,

    @ColumnInfo(name = "is_agreed")
    val isAgreed: Boolean,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "gender")
    val gender: Gender,

    @ColumnInfo(name = "time")
    val time: Long
) {

    companion object {

        fun from(from: Message.Reply) =
            MessageEntity(
                null,
                from.messageType,
                from.loginWay,
                from.isAgreed,
                from.id,
                from.name,
                from.gender,
                from.time
            )
    }
}