package com.sabgil.bbuckkugi.ui.namesetting

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.SingleLiveEvent
import com.sabgil.bbuckkugi.pref.AppSharedPreference

class NameSettingViewModel @ViewModelInject constructor(
    private val appSharedPreference: AppSharedPreference
) : BaseViewModel() {

    val inputName = MutableLiveData("")

    private val _nicknameStoreSuccess = SingleLiveEvent<Nothing>()
    val nicknameStoreSuccess: LiveData<Nothing> get() = _nicknameStoreSuccess

    fun storeNickname() {
        val inputtedName = inputName.value
        appSharedPreference.name = inputtedName
        _nicknameStoreSuccess.call()
    }
}