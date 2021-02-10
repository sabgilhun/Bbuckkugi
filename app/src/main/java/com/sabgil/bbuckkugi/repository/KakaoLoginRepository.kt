package com.sabgil.bbuckkugi.repository

import com.kakao.sdk.user.model.User

interface KakaoLoginRepository {

    val isKakaoTalkLoginAvailable: Boolean

    suspend fun loginWithKakaoTalk()

    suspend fun loginWithKakaoAccount()

    suspend fun loadUserInfo(): User?

    suspend fun unlink()
}