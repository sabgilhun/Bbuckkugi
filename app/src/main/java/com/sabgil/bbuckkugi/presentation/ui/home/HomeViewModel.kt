package com.sabgil.bbuckkugi.presentation.ui.home

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sabgil.bbuckkugi.base.BaseViewModel
import com.sabgil.bbuckkugi.data.model.Message
import com.sabgil.bbuckkugi.data.repository.ConnectionManager
import kotlinx.coroutines.Job
import kotlin.random.Random

class HomeViewModel @ViewModelInject constructor(
    private val connectionManager: ConnectionManager
) : BaseViewModel() {

    private var discoveryJob: Job? = null
    private var advertiseJob: Job? = null
    private var endpointId: String? = null

    private val _receiveData = MutableLiveData<String>()
    val receiveData: LiveData<String> get() = _receiveData

    val inputData = MutableLiveData<String>()

    fun startAdvertising() {
        val name = inputData.value ?: "default host name"
        advertiseJob = connectionManager.startAdvertising(name)
            .collectResult {
                success {
                    Log.i("ConnectionTestTag", "advertise s")
                    advertiseJob?.cancel()
                }
                error {
                    Log.i("ConnectionTestTag", "advertise e")
                    showErrorMessage(it)
                }
            }
    }

    fun startDiscovery() {
        discoveryJob = connectionManager.startDiscovery()
            .collectResult {
                success {
                    discoveryJob?.cancel()
                    connectRemote(it.endpointId)
                }
                error {
                    Log.i("ConnectionTestTag", "discovery e")
                    showErrorMessage(it)
                }
            }
    }

    fun sendData() {
        val id = endpointId ?: return
        connectionManager.sendMessage(id, Message.MessageCard(Random.nextInt().rem(10)))
            .collectResult {
                error {
                    showErrorMessage(it)
                }
            }
    }

    private fun acceptRemote(endpointId: String) {
        this.endpointId = endpointId
        connectionManager.acceptRemote(endpointId)
            .collectResult {
                success {
                    Log.i("ConnectionTestTag", "accept s")
//                    _receiveData.value = it.byteValue.toString()
                }
                error {
                    Log.i("ConnectionTestTag", "accept e")
                    showErrorMessage(it)
                }
            }
    }

    private fun connectRemote(endpointId: String) {
        this.endpointId = endpointId
        connectionManager.connectRemote(endpointId)
            .collectResult {
                success {
                    Log.i("ConnectionTestTag", "connect s")
//                    _receiveData.value = it.byteValue.toString()
                }
                error {
                    Log.i("ConnectionTestTag", "connect e")
                    showErrorMessage(it)
                }
            }
    }
}