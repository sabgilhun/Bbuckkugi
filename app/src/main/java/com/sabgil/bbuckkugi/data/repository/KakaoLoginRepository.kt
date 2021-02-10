package com.sabgil.bbuckkugi.data.repository

import com.sabgil.bbuckkugi.data.model.UserInfo

interface KakaoLoginRepository {

    val isKakaoTalkLoginAvailable: Boolean

    suspend fun loginWithKakaoTalk()

    suspend fun loginWithKakaoAccount()

    suspend fun loadUserInfo(): UserInfo

    suspend fun unlink()
}