package com.sabgil.bbuckkugi.presentation.ui.send

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.common.SingleLiveEvent
import com.sabgil.bbuckkugi.data.model.Message
import com.sabgil.bbuckkugi.data.repository.ConnectionManager
import com.sabgil.bbuckkugi.data.repository.ServerTimeRepository
import kotlinx.coroutines.Dispatchers

class SendViewModel @ViewModelInject constructor(
    private val connectionManager: ConnectionManager,
    private val serverTimeRepository: ServerTimeRepository
) : BaseViewModel() {

    private val _state = MutableLiveData(State.AWAIT_CONNECTION)
    val state: LiveData<State> get() = _state

    private val _receivedReply = SingleLiveEvent<Message>()
    val receivedReply: LiveData<Message> get() = _receivedReply

    fun connect(endpointId: String) {
        connectionManager.connectRemote(endpointId)
            .collectResult(Dispatchers.Main) {
                success {
                    if (it is Message.Start) {
                        _state.value = State.COMPLETE_CONNECTION
                    } else if (it is Message.Agree || it is Message.Reject) {
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
        connectionManager.sendMessage(endpointId, Message.MessageCard(cardType))
            .collectComplete(Dispatchers.Main) {
                error {
                    showErrorMessage(it)
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