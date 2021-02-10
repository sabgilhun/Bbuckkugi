package com.sabgil.bbuckkugi.ui.start

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.SingleLiveEvent
import com.sabgil.bbuckkugi.pref.AppSharedPreference
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartViewModel @ViewModelInject constructor(
    private val appSharedPreference: AppSharedPreference
) : BaseViewModel() {

    private val _isExistRequiredData = SingleLiveEvent<Boolean>()
    val isExistRequiredData: LiveData<Boolean> get() = _isExistRequiredData

    fun checkRequiredUserData() {
        viewModelScope.launch {
            delay(1000)

            val isExist = with(appSharedPreference) {
                loginWay != null && id != null && gender != null && name != null
            }

            if (isExist) {
                _isExistRequiredData.value = true
            } else {
                clearUserData()
                _isExistRequiredData.value = false
            }
        }
    }

    private fun clearUserData() {
        with(appSharedPreference) {
            loginWay = null
            id = null
            gender = null
            name = null
            profileImageUrl = null
        }
    }
}