package com.sabgil.bbuckkugi.presentation.ui.send

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.data.model.Message
import com.sabgil.bbuckkugi.data.repository.ConnectionManager

class SendViewModel @ViewModelInject constructor(
    private val connectionManager: ConnectionManager
) : BaseViewModel() {

    private val _isConnected = MutableLiveData(false)
    val isConnected: LiveData<Boolean> get() = _isConnected

    fun connect(endpointId: String) {
        connectionManager.connectRemote(endpointId)
            .collectResult {
                success {
                    if (it is Message.Start) {
                        _isConnected.value = true
                    }
                }
                error {

                }
            }
    }
}