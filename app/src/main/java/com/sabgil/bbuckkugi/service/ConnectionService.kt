package com.sabgil.bbuckkugi.service

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.common.doNotAnything
import com.sabgil.bbuckkugi.common.ext.collectOnMain
import com.sabgil.bbuckkugi.model.Data
import com.sabgil.bbuckkugi.pref.AppSharedPreference
import com.sabgil.bbuckkugi.repository.ConnectionManager
import com.sabgil.bbuckkugi.service.ConnectionService.Status.*
import com.sabgil.bbuckkugi.service.channel.CommunicationChannel
import com.sabgil.bbuckkugi.service.channel.ConnectionRequestChannel
import com.sabgil.bbuckkugi.service.channel.DiscoveryChannel
import com.sabgil.bbuckkugi.service.channel.DiscoveryChannel.Action.DISCOVERY_START
import com.sabgil.bbuckkugi.service.channel.DiscoveryChannel.Action.DISCOVERY_STOP
import com.sabgil.bbuckkugi.ui.receive.ReceiveActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ConnectionService : LifecycleService() {

    @Inject
    lateinit var connectionManager: ConnectionManager

    @Inject
    lateinit var appSharedPreference: AppSharedPreference

    @Inject
    lateinit var discoveryChannel: DiscoveryChannel

    @Inject
    lateinit var connectionRequestChannel: ConnectionRequestChannel

    @Inject
    lateinit var communicationChannel: CommunicationChannel

    private val errorHandler = CoroutineExceptionHandler { _, _ ->

    }

    private val backgroundDispatcher = errorHandler + Dispatchers.Default

    private var status: Status = None
        set(value) {
            Timber.i(field.toString())
            previousJob?.cancel()
            startJob(value)
            field = value
        }

    private var previousJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        status = Advertising
        registerToChannel()
    }

    private fun registerToChannel() {
        discoveryChannel.registerHost(this) {
            status = when (it) {
                DISCOVERY_START -> Discovering
                DISCOVERY_STOP -> Advertising
            }
        }

        connectionRequestChannel.registerHost(this) {
            status = Connecting(false, it)
        }

        communicationChannel.registerHost(this) {
            sendDataToEndpoint(it)
        }
    }

    private fun startJob(toBe: Status) {
        previousJob = lifecycleScope.launch(backgroundDispatcher) {
            delay(500)
            when (toBe) {
                None -> doNotAnything()
                Advertising -> startAdvertising()
                Discovering -> startDiscovering()
                is Connecting -> startConnecting(toBe)
            }
        }
    }

    private suspend fun startAdvertising() {
        connectionManager.startAdvertising(requireNotNull(appSharedPreference.nickname))
            .collectOnMain {
                status = when (it) {
                    is Result.Success -> Connecting(true, it.result.endpointId)
                    is Result.Failure -> Advertising
                }
            }
    }

    private suspend fun startDiscovering() {
        connectionManager.startDiscovery()
            .collectOnMain {
                when (it) {
                    is Result.Success -> discoveryChannel.sendResult(it)
                    is Result.Failure -> {
                        discoveryChannel.sendResult(it)
                        status = Advertising
                    }
                }
            }
    }

    private suspend fun startConnecting(connecting: Connecting) {
        if (connecting.isFromRemote) {
            connectionManager.acceptRemote(connecting.endpointId)
                .collectOnMain {
                    when (it) {
                        is Result.Success -> if (it.result is Data.Message) {
                            ReceiveActivity.start(this, it.result)
                        } else {
                            connectionRequestChannel.sendResult(it)
                        }
                        is Result.Failure -> status = Advertising
                    }
                }
        } else {
            connectionManager.connectRemote(connecting.endpointId)
                .collectOnMain {
                    when (it) {
                        is Result.Success -> {
                            if (it.result is Data.Start) {
                                connectionRequestChannel.sendResult(it)
                            } else {
                                communicationChannel.sendRxData(it)
                            }
                        }
                        is Result.Failure -> {
                            connectionRequestChannel.sendResult(it)
                            communicationChannel.sendRxData(it)
                            status = Advertising
                        }
                    }
                }
        }
    }

    private fun sendDataToEndpoint(data: Data) {
        val currentStatus = status
        if (currentStatus is Connecting) {
            lifecycleScope.launch(backgroundDispatcher) {
                connectionManager.sendData(currentStatus.endpointId, data).collect()
            }
        }
    }

    private sealed class Status {
        object None : Status()
        object Advertising : Status()
        object Discovering : Status()
        class Connecting(val isFromRemote: Boolean, val endpointId: String) : Status()
    }
}