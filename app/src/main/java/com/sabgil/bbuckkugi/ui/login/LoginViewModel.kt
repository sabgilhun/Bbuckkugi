package com.sabgil.bbuckkugi.ui.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.SingleLiveEvent
import com.sabgil.bbuckkugi.pref.AppSharedPreference

class LoginViewModel @ViewModelInject constructor(
    private val appSharedPreference: AppSharedPreference
) : BaseViewModel() {

    private val _needNicknameSetting = SingleLiveEvent<Boolean>()
    val needNicknameSetting: LiveData<Boolean> = _needNicknameSetting

    fun checkNicknameExistence() {
        _needNicknameSetting.value = appSharedPreference.nickname == null
    }
}