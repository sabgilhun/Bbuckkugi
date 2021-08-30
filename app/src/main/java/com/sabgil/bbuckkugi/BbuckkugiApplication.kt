package com.sabgil.bbuckkugi

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.nhn.android.naverlogin.OAuthLogin
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BbuckkugiApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        initKakaoSdk()
        initNaverSdk()
    }

    private fun initKakaoSdk() {
        KakaoSdk.init(
            this,
            BuildConfig.KAKAO_SDK_KEY
        )
    }

    private fun initNaverSdk() {
        OAuthLogin.getInstance().init(
            this,
            BuildConfig.NAVER_API_CLIENTS_ID,
            BuildConfig.NAVER_API_CLIENTS_SECRET,
            "법무법인 동광"
        )
    }
}