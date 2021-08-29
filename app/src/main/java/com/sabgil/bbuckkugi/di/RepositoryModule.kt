package com.sabgil.bbuckkugi.di

import com.sabgil.bbuckkugi.data.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideConnectionManager(
        connectionManagerImpl: ConnectionManagerImpl
    ): ConnectionManager

    @Binds
    abstract fun provideServerTimeRepository(
        serverTimeRepositoryImpl: ServerTimeRepositoryImpl
    ): ServerTimeRepository

    @Binds
    abstract fun provideMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository
}