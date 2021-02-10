package com.sabgil.bbuckkugi.repository

import com.kakao.sdk.user.model.User
import com.sabgil.bbuckkugi.model.UserInfo

interface KakaoLoginRepository {

    val isKakaoTalkLoginAvailable: Boolean

    suspend fun loginWithKakaoTalk()

    suspend fun loginWithKakaoAccount()

    suspend fun loadUserInfo(): UserInfo

    suspend fun unlink()
}