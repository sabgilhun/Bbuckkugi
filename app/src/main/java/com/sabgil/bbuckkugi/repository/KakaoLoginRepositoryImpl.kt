package com.sabgil.bbuckkugi.repository

import android.content.Context
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.rx
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.kakao.sdk.user.rx
import kotlinx.coroutines.rx2.await
import javax.inject.Inject

class KakaoLoginRepositoryImpl @Inject constructor(
    private val context: Context
) : KakaoLoginRepository {

    override val isKakaoTalkLoginAvailable: Boolean
        get() = LoginClient.instance.isKakaoTalkLoginAvailable(context)

    override suspend fun loginWithKakaoTalk() {
        LoginClient.rx.loginWithKakaoTalk(context).await()
    }

    override suspend fun loginWithKakaoAccount() {
        LoginClient.rx.loginWithKakaoAccount(context).await()
    }

    override suspend fun loadUserInfo(): User? = UserApiClient.rx.me().await()

    override suspend fun unlink() = UserApiClient.rx.unlink().await()
}