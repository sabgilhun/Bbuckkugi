package com.sabgil.bbuckkugi.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kakao.sdk.user.model.User
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.doNotAnything
import com.sabgil.bbuckkugi.pref.AppSharedPreference
import com.sabgil.bbuckkugi.repository.KakaoLoginRepository
import kotlinx.coroutines.launch

typealias KakaoGender = com.kakao.sdk.user.model.Gender

class LoginViewModel @ViewModelInject constructor(
    private val appSharedPreference: AppSharedPreference,
    private val kakaoLoginRepository: KakaoLoginRepository
) : BaseViewModel() {

    private val _userInfo = MutableLiveData<User>()

    private val _canGoNext = MediatorLiveData<Boolean>()
    val canGoNext: LiveData<Boolean> get() = _canGoNext

    val userName = MutableLiveData<String>()

    val isSelectedMale = MutableLiveData<Boolean>()

    val isSelectedFemale = MutableLiveData<Boolean>()

    val loginSuccess: LiveData<Boolean> = Transformations.map(_userInfo) { it != null }

    init {
        initCnaGoNextObserver()
    }

    fun loginWithKakao() {
        viewModelScope.launch(errorHandler) {
            if (kakaoLoginRepository.isKakaoTalkLoginAvailable) {
                kakaoLoginRepository.loginWithKakaoTalk()
            } else {
                kakaoLoginRepository.loginWithKakaoAccount()
            }

            val userInfo = requireNotNull(kakaoLoginRepository.loadUserInfo())
            _userInfo.value = userInfo

            when (userInfo.kakaoAccount?.gender) {
                KakaoGender.FEMALE -> {
                    isSelectedFemale.value = true
                    isSelectedMale.value = false
                }
                KakaoGender.MALE -> {
                    isSelectedFemale.value = false
                    isSelectedMale.value = true
                }
                else -> doNotAnything()
            }

            userName.value = userInfo.kakaoAccount?.profile?.nickname
        }
    }

    private fun initCnaGoNextObserver() {
        val observer = Observer<Any> {
            val userName = userName.value
            _canGoNext.value = _userInfo.value != null &&
                    isSelectedFemale.value != null &&
                    isSelectedMale.value != null &&
                    userName != null && userName.length <= 10
        }
        _canGoNext.addSource(_userInfo, observer)
        _canGoNext.addSource(isSelectedFemale, observer)
        _canGoNext.addSource(isSelectedMale, observer)
        _canGoNext.addSource(userName, observer)
    }
}