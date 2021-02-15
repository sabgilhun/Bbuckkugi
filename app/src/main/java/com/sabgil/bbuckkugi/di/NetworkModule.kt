package com.sabgil.bbuckkugi.di

import com.sabgil.bbuckkugi.data.api.NaverLoginApi
import com.sabgil.bbuckkugi.data.api.ServerTimeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val NAVER_LOGIN_BASE_URL = "https://openapi.naver.com/v1/nid/"
    private const val SERVER_TIME_BASE_URL = "http://worldtimeapi.org/"

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .build()


    @Provides
    @Named("naverLogin")
    fun provideNaverRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(NAVER_LOGIN_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideNaverLoginApi(
        @Named("naverLogin") retrofit: Retrofit
    ): NaverLoginApi = retrofit.create(NaverLoginApi::class.java)

    @Provides
    @Named("serverTime")
    fun provideServerTimeRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(SERVER_TIME_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideServerTimeApi(
        @Named("serverTime") retrofit: Retrofit
    ): ServerTimeApi = retrofit.create(ServerTimeApi::class.java)
}