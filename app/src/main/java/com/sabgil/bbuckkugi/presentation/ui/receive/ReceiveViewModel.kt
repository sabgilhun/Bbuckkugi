package com.sabgil.bbuckkugi.presentation.ui.receive

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.SingleLiveEvent
import com.sabgil.bbuckkugi.data.model.Message
import com.sabgil.bbuckkugi.data.repository.ConnectionManager
import kotlinx.coroutines.Dispatchers

class ReceiveViewModel @ViewModelInject constructor(
    private val connectionManager: ConnectionManager
) : BaseViewModel() {

    private val _startTimer = SingleLiveEvent<Nothing>()
    val startTimer: LiveData<Nothing> get() = _startTimer

    private val _isReceived = MutableLiveData<Message.MessageCard>()
    val isReceived: LiveData<Message.MessageCard> get() = _isReceived

    fun acceptRemote(endpointId: String) {
        connectionManager.acceptRemote(endpointId)
            .collectResult(Dispatchers.Main) {
                success {
                    if (it is Message.MessageCard) {
                        _startTimer.call()
                        _isReceived.value = it
                    }
                }
                error {
                    showErrorMessage(it)
                }
            }
    }
}