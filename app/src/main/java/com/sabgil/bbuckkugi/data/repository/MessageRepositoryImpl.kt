package com.sabgil.bbuckkugi.data.repository

import com.sabgil.bbuckkugi.data.db.MessageDao
import com.sabgil.bbuckkugi.data.entities.MessageEntity
import com.sabgil.bbuckkugi.data.model.Message
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao
) : MessageRepository {
    override suspend fun saveMessage(message: Message.Reply) {
        messageDao.insert(MessageEntity.from(message))
    }

    override suspend fun selectAllMessage(): List<Message.Reply> {
        return messageDao.selectAllMessage().map { Message.Reply.from(it) }
    }
}