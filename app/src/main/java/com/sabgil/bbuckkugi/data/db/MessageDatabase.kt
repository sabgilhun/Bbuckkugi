package com.sabgil.bbuckkugi.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sabgil.bbuckkugi.data.entities.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class MessageDatabase : RoomDatabase() {

    abstract fun getMessageDao(): MessageDao
}