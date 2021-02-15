package com.sabgil.bbuckkugi.presentation.ui.send

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.SingleLiveEvent
import com.sabgil.bbuckkugi.data.model.Message
import com.sabgil.bbuckkugi.data.repository.ConnectionManager
import com.sabgil.bbuckkugi.data.repository.ServerTimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendViewModel @ViewModelInject constructor(
    private val connectionManager: ConnectionManager,
    private val serverTimeRepository: ServerTimeRepository
) : BaseViewModel() {

    private val _state = MutableLiveData(State.AWAIT_CONNECTION)
    val state: LiveData<State> get() = _state

    private val _receivedReply = SingleLiveEvent<Message.Reply>()
    val receivedReply: LiveData<Message.Reply> get() = _receivedReply

    fun connect(endpointId: String) {
        connectionManager.connectRemote(endpointId)
            .collectResult(Dispatchers.Main) {
                success {
                    if (it is Message.Start) {
                        _state.value = State.COMPLETE_CONNECTION
                    } else if (it is Message.Reply) {
                        _receivedReply.value = it
                        _state.value = State.COMPLETE_REPLY
                    }
                }
                error {
                    showErrorMessage(it)
                }
            }
    }

    fun sendMessage(endpointId: String, cardType: Int) {
        _state.value = State.AWAIT_REPLY
        viewModelScope.launch {
            val times = serverTimeRepository.getSeoulTime()
            connectionManager.sendMessage(endpointId, Message.Send(cardType))
                .collectComplete(Dispatchers.Main) {
                    error {
                        showErrorMessage(it)
                    }
                }
        }
    }

    enum class State(val desc: String? = null) {

        AWAIT_CONNECTION("상대방 연결을 기다리는 중입니다."),

        COMPLETE_CONNECTION,

        AWAIT_REPLY("상대방 답장을 기다리는 중입니다."),

        COMPLETE_REPLY
    }
}