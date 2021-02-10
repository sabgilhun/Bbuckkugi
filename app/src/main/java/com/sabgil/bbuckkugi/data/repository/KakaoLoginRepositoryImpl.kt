package com.sabgil.bbuckkugi.data.repository

import android.app.Activity
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.rx
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.kakao.sdk.user.rx
import com.sabgil.bbuckkugi.data.model.UserInfo
import com.sabgil.bbuckkugi.data.model.enums.Gender
import com.sabgil.bbuckkugi.data.model.enums.LoginWay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.rx2.await
import javax.inject.Inject

typealias KakaoGender = com.kakao.sdk.user.model.Gender

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

    override suspend fun loadUserInfo(): UserInfo =
        UserApiClient.rx.me()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.toUserInfo() }
            .await()

    override suspend fun unlink() = UserApiClient.rx.unlink()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .await()

    private fun User.toUserInfo() = UserInfo(
        loginWay = LoginWay.KAKAO,
        id = id.toString(),
        gender = when (kakaoAccount?.gender) {
            KakaoGender.FEMALE -> {
                Gender.FEMALE
            }
            KakaoGender.MALE -> {
                Gender.MALE
            }
            else -> null
        },
        name = kakaoAccount?.profile?.nickname,
        profileImageUrl = kakaoAccount?.profile?.profileImageUrl
    )
}