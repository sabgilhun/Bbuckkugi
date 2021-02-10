package com.sabgil.bbuckkugi.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.model.UserInfo
import com.sabgil.bbuckkugi.model.enums.Gender
import com.sabgil.bbuckkugi.pref.AppSharedPreference
import com.sabgil.bbuckkugi.repository.KakaoLoginRepository
import com.sabgil.bbuckkugi.repository.NaverLoginRepository
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val appSharedPreference: AppSharedPreference,
    private val kakaoLoginRepository: KakaoLoginRepository,
    private val naverLoginRepository: NaverLoginRepository
) : BaseViewModel() {

    private val _userInfo = MutableLiveData<UserInfo>()

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
            setupWithUserInfo(kakaoLoginRepository.loadUserInfo())
        }
    }

    fun loginWithNaver() {
        viewModelScope.launch(errorHandler) {
            naverLoginRepository.login()
            setupWithUserInfo(naverLoginRepository.loadUserInfo())
        }
    }

    private fun initCnaGoNextObserver() {
        val observer = Observer<Any> {
            _canGoNext.value = checkCanGoNext()
        }
        _canGoNext.addSource(_userInfo, observer)
        _canGoNext.addSource(isSelectedFemale, observer)
        _canGoNext.addSource(isSelectedMale, observer)
        _canGoNext.addSource(userName, observer)
    }

    private fun checkCanGoNext() = _userInfo.value != null &&
            (isSelectedFemale.value == true || isSelectedMale.value == true) &&
            userName.value.let { it != null && it.length <= 10 }

    private fun setupWithUserInfo(userInfo: UserInfo) {
        _userInfo.value = userInfo
        isSelectedFemale.value = userInfo.gender == Gender.FEMALE
        isSelectedMale.value = userInfo.gender == Gender.MALE
        userName.value = userInfo.name
    }
}