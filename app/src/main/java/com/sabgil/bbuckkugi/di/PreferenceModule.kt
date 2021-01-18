package com.sabgil.bbuckkugi.di

import com.sabgil.bbuckkugi.pref.AppSharedPreference
import com.sabgil.bbuckkugi.pref.AppSharedPreferenceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferenceModule {

    @Binds
    abstract fun provideAppSharedPreference(
        appSharedPreferenceImpl: AppSharedPreferenceImpl
    ): AppSharedPreference
}