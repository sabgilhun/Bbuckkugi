package com.sabgil.bbuckkugi.di

import com.sabgil.bbuckkugi.repository.ConnectionManager
import com.sabgil.bbuckkugi.repository.ConnectionManagerImpl
import com.sabgil.bbuckkugi.repository.KakaoLoginRepository
import com.sabgil.bbuckkugi.repository.KakaoLoginRepositoryImpl
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
    abstract fun provideKakaoLoginRepository(
        kakaoLoginRepositoryImpl: KakaoLoginRepositoryImpl
    ): KakaoLoginRepository
}