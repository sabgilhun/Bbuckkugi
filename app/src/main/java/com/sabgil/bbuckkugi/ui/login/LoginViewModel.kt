package com.sabgil.bbuckkugi.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.SingleLiveEvent
import com.sabgil.bbuckkugi.pref.AppSharedPreference
import com.sabgil.bbuckkugi.repository.KakaoLoginRepository
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val appSharedPreference: AppSharedPreference,
    private val kakaoLoginRepository: KakaoLoginRepository
) : BaseViewModel() {

    fun loginWithKakao() {
        viewModelScope.launch(ioErrorHandler) {
            if(kakaoLoginRepository.isKakaoTalkLoginAvailable) {
                kakaoLoginRepository.loginWithKakaoTalk()
            } else {
                kakaoLoginRepository.loginWithKakaoAccount()
            }
        }
    }
}