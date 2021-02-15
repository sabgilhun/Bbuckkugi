package com.sabgil.bbuckkugi.di

import com.sabgil.bbuckkugi.data.repository.ConnectionManager
import com.sabgil.bbuckkugi.data.repository.ConnectionManagerImpl
import com.sabgil.bbuckkugi.data.repository.ServerTimeRepository
import com.sabgil.bbuckkugi.data.repository.ServerTimeRepositoryImpl
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
}