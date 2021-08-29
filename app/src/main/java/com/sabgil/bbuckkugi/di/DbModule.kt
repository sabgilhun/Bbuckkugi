package com.sabgil.bbuckkugi.di

import android.content.Context
import androidx.room.Room
import com.sabgil.bbuckkugi.data.db.MessageDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Provides
    fun provideMessageDatabase(
        context: Context
    ): MessageDatabase = Room.databaseBuilder(
        context.applicationContext,
        MessageDatabase::class.java,
        DB_NAME
    ).build()

    @Provides
    fun provideMessageDao(messageDatabase: MessageDatabase) =
        messageDatabase.getMessageDao()

    companion object {
        private const val DB_NAME = "message.db"
    }
}