package com.sabgil.bbuckkugi.presentation.ui.ladder

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.SingleLiveEvent

class LadderViewModel @ViewModelInject constructor() : BaseViewModel() {

    private val _uiState = SingleLiveEvent<UiState>()
    val uiState: LiveData<UiState> = _uiState

    private val _playerResultList = MutableLiveData<List<PlayerResultItem>>()
    val playerResultList: LiveData<List<PlayerResultItem>> = _playerResultList

    private val _playerCount = MutableLiveData(4)
    val playerCount: LiveData<Int> = _playerCount

    private val _gameFinishEvent = SingleLiveEvent<Unit>()
    val gameFinishEvent: LiveData<Unit> = _gameFinishEvent

    fun playerCount() = playerCount.value ?: 0

    fun onClickGameStart() {
        _uiState.value = UiState.RUNNING
    }

    fun onClickRestart() {
        _uiState.value = UiState.SETTING
        _playerResultList.value = emptyList()
    }

    fun onClickPlusPlayer() {
        val count = _playerCount.value ?: 0
        if (count >= 12) return
        _playerCount.value = count + 1
    }

    fun onClickMinusPlayer() {
        val count = _playerCount.value ?: 0
        if (count <= 2) return
        _playerCount.value = count - 1
    }

    fun onClickGameFinish() {
        _gameFinishEvent.value = Unit
    }

    fun addPlayerResult(player: String, reword: String) {
        val list = (_playerResultList.value ?: emptyList()) + listOf(PlayerResultItem(player, reword))
        _playerResultList.value = list.sortedBy { it.name.replace("\\D".toRegex(), "").toInt() }
    }

    enum class UiState {
        SETTING,
        RUNNING,
    }
}