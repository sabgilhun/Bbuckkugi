package com.sabgil.bbuckkugi.ui.start

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.SingleLiveEvent
import com.sabgil.bbuckkugi.pref.AppSharedPreference

class StartViewModel @ViewModelInject constructor(
    private val appSharedPreference: AppSharedPreference
) : BaseViewModel() {

    private val _isExistRequiredData = SingleLiveEvent<Boolean>()
    val isExistRequiredData: LiveData<Boolean> get() = _isExistRequiredData

    fun checkRequiredUserData() {
        if (appSharedPreference.id != null
            && appSharedPreference.nickname != null
            && appSharedPreference.gender != null
            && appSharedPreference.loginWay != null
        ) {
            _isExistRequiredData.value = true
        } else {
            clearUserData()
            _isExistRequiredData.value = false
        }
    }

    private fun clearUserData() {
        appSharedPreference.id = null
        appSharedPreference.nickname = null
        appSharedPreference.gender = null
        appSharedPreference.loginWay = null
    }
}