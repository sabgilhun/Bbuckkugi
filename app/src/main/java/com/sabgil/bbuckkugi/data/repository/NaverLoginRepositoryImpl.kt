package com.sabgil.bbuckkugi.data.repository

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.sabgil.bbuckkugi.data.api.NaverLoginApi
import com.sabgil.bbuckkugi.data.api.NaverLoginUserInfoResponse
import com.sabgil.bbuckkugi.common.doNotAnything
import com.sabgil.bbuckkugi.data.model.UserInfo
import com.sabgil.bbuckkugi.data.model.enums.Gender
import com.sabgil.bbuckkugi.data.model.enums.LoginWay
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class NaverLoginRepositoryImpl @Inject constructor(
    private val context: Context,
    private val activity: Activity,
    private val naverLoginApi: NaverLoginApi
) : NaverLoginRepository {

    private val oAuthLogin = OAuthLogin.getInstance()

    @SuppressLint("HandlerLeak")
    private val emptyOAuthLoginHandler = object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            doNotAnything()
        }
    }

    override suspend fun login() = suspendCancellableCoroutine<Unit> { cont ->
        @SuppressLint("HandlerLeak")
        val oAuthLoginHandler = object : OAuthLoginHandler() {
            override fun run(success: Boolean) {
                if (success) {
                    cont.resume(Unit)
                } else {
                    cont.resumeWithException(IllegalStateException())
                }
            }
        }

        cont.invokeOnCancellation { OAuthLogin.mOAuthLoginHandler = emptyOAuthLoginHandler }
        oAuthLogin.startOauthLoginActivity(activity, oAuthLoginHandler)
    }

    override suspend fun loadUserInfo(): UserInfo {
        val accessToken = requireNotNull(oAuthLogin.getAccessToken(context))
        val tokenType = requireNotNull(oAuthLogin.getTokenType(context))
        val header = "$tokenType $accessToken"

        return requireNotNull(naverLoginApi.loadUserInfo(header).response).toUserInfo()
    }

    private fun NaverLoginUserInfoResponse.Response.toUserInfo() = UserInfo(
        loginWay = LoginWay.NAVER,
        id = id,
        gender = when (gender) {
            "F" -> Gender.FEMALE
            "M" -> Gender.MALE
            else -> null
        },
        name = if (name.isNullOrEmpty()) {
            null
        } else {
            name
        },
        profileImageUrl = if (profileImageUrl.isNullOrEmpty()) {
            null
        } else {
            profileImageUrl
        },
    )
}