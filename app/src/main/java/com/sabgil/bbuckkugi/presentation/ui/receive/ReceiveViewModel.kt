package com.sabgil.bbuckkugi.presentation.ui.receive

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.SingleLiveEvent
import com.sabgil.bbuckkugi.data.model.Message
import com.sabgil.bbuckkugi.data.pref.AppSharedPreference
import com.sabgil.bbuckkugi.data.repository.ConnectionManager
import com.sabgil.bbuckkugi.data.repository.ServerTimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReceiveViewModel @ViewModelInject constructor(
    private val connectionManager: ConnectionManager,
    private val serverTimeRepository: ServerTimeRepository,
    private val appSharedPreference: AppSharedPreference
) : BaseViewModel() {

    private val _startTimer = SingleLiveEvent<Nothing>()
    val startTimer: LiveData<Nothing> get() = _startTimer

    private val _isReceived = MutableLiveData<Message.Send>()
    val isReceived: LiveData<Message.Send> get() = _isReceived

    private val _replyDone = SingleLiveEvent<Nothing>()
    val replyDone: LiveData<Nothing> get() = _replyDone

    fun acceptRemote(endpointId: String) {
        connectionManager.acceptRemote(endpointId)
            .collectResult(Dispatchers.Main) {
                success {
                    if (it is Message.Send) {
                        _startTimer.call()
                        _isReceived.value = it
                    }
                }
                error {
                    showErrorMessage(it)
                }
            }
    }

    fun replayMessage(endpointId: String, isAgreed: Boolean) {
        viewModelScope.launch {
            val messageType = _isReceived.value?.messageCardIndex ?: return@launch
            val loginWay = appSharedPreference.loginWay ?: return@launch
            val id = appSharedPreference.id ?: return@launch
            val name = appSharedPreference.name ?: return@launch
            val gender = appSharedPreference.gender ?: return@launch
            val currentTime = serverTimeRepository.getSeoulTime()

            val reply = Message.Reply(
                messageType,
                loginWay,
                isAgreed,
                id,
                name,
                gender,
                currentTime
            )

            connectionManager.sendMessage(endpointId, reply)
                .collectComplete {
                    complete {
                        _replyDone.call()
                    }
                    error {
                        showErrorMessage(it)
                    }
                }
        }
    }
}