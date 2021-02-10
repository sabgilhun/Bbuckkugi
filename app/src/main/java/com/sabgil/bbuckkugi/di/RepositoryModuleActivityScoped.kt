package com.sabgil.bbuckkugi.di

import com.sabgil.bbuckkugi.repository.KakaoLoginRepository
import com.sabgil.bbuckkugi.repository.KakaoLoginRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class RepositoryModuleActivityScoped {

    @Binds
    abstract fun provideKakaoLoginRepository(
        kakaoLoginRepositoryImpl: KakaoLoginRepositoryImpl
    ): KakaoLoginRepository
}