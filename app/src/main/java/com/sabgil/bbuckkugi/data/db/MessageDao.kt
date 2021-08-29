package com.sabgil.bbuckkugi.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sabgil.bbuckkugi.data.entities.MessageEntity

@Dao
abstract class MessageDao {

    @Query("SELECT * FROM MESSAGE")
    abstract suspend fun selectAllMessage(): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(messageEntity: MessageEntity)
}