package com.sabgil.bbuckkugi.data.repository

import com.sabgil.bbuckkugi.data.model.Message

interface MessageRepository {

    suspend fun saveMessage(message: Message.Reply)

    suspend fun selectAllMessage(): List<Message.Reply>
}