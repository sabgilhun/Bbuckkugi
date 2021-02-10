package com.sabgil.bbuckkugi.repository

import android.app.Activity
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.rx
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.kakao.sdk.user.rx
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.rx2.await
import javax.inject.Inject

class KakaoLoginRepositoryImpl @Inject constructor(
    private val activity: Activity
) : KakaoLoginRepository {

    override val isKakaoTalkLoginAvailable: Boolean
        get() = LoginClient.instance.isKakaoTalkLoginAvailable(activity)

    override suspend fun loginWithKakaoTalk() {
        LoginClient.rx.loginWithKakaoTalk(activity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .await()
    }

    override suspend fun loginWithKakaoAccount() {
        LoginClient.rx.loginWithKakaoAccount(activity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .await()
    }

    override suspend fun loadUserInfo(): User? =
        UserApiClient.rx.me()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .await()

    override suspend fun unlink() = UserApiClient.rx.unlink()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .await()
}