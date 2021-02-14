package com.sabgil.bbuckkugi.presentation.ui.send

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.SingleLiveEvent
import com.sabgil.bbuckkugi.data.model.Message
import com.sabgil.bbuckkugi.data.repository.ConnectionManager
import kotlinx.coroutines.Dispatchers

class SendViewModel @ViewModelInject constructor(
    private val connectionManager: ConnectionManager
) : BaseViewModel() {

    private val _isConnected = MutableLiveData(false)
    val isConnected: LiveData<Boolean> get() = _isConnected

    private val _sendSuccessEvent = SingleLiveEvent<Nothing>()
    val sendSuccessEvent: LiveData<Nothing> get() = _sendSuccessEvent

    fun connect(endpointId: String) {
        connectionManager.connectRemote(endpointId)
            .collectResult(Dispatchers.Main) {
                success {
                    if (it is Message.Start) {
                        _isConnected.value = true
                    }
                }
                error {
                    showErrorMessage(it)
                }
            }
    }

    fun sendMessage(endpointId: String, cardType: Int) {
        connectionManager.sendMessage(endpointId, Message.MessageCard(cardType))
            .collectComplete(Dispatchers.Main) {
                complete {
                    _sendSuccessEvent.call()
                }
                error {
                    showErrorMessage(it)
                }
            }
    }
}