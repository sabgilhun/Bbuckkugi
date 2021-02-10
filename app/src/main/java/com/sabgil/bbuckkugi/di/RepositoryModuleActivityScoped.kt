package com.sabgil.bbuckkugi.di

import com.sabgil.bbuckkugi.data.repository.KakaoLoginRepository
import com.sabgil.bbuckkugi.data.repository.KakaoLoginRepositoryImpl
import com.sabgil.bbuckkugi.data.repository.NaverLoginRepository
import com.sabgil.bbuckkugi.data.repository.NaverLoginRepositoryImpl
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

    @Binds
    abstract fun provideNaverLoginRepository(
        naverLoginRepositoryImpl: NaverLoginRepositoryImpl
    ): NaverLoginRepository
}