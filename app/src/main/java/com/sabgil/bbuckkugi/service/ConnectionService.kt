package com.sabgil.bbuckkugi.service

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.sabgil.bbuckkugi.common.Data.Failure
import com.sabgil.bbuckkugi.common.Data.Success
import com.sabgil.bbuckkugi.common.ext.collectOnMain
import com.sabgil.bbuckkugi.model.AdvertisingResult
import com.sabgil.bbuckkugi.model.Message
import com.sabgil.bbuckkugi.pref.AppSharedPreference
import com.sabgil.bbuckkugi.repository.ConnectionManager
import com.sabgil.bbuckkugi.service.channel.CommunicationChannel
import com.sabgil.bbuckkugi.service.channel.ConnectionRequestChannel
import com.sabgil.bbuckkugi.service.channel.DiscoveryChannel
import com.sabgil.bbuckkugi.service.channel.DiscoveryChannel.Action.DISCOVERY_START
import com.sabgil.bbuckkugi.service.channel.DiscoveryChannel.Action.DISCOVERY_STOP
import com.sabgil.bbuckkugi.ui.receive.ReceiveActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val backgroundDispatcher = Dispatchers.Default

    private var discoveryJob: Job? = null

    private var connectedEndpointId: String? = null

    override fun onCreate() {
        super.onCreate()
        registerToChannel()
        startAdvertising()
    }

    private fun registerToChannel() {
        discoveryChannel.registerHost(this) {
            when (it) {
                DISCOVERY_START -> startDiscovering()
                DISCOVERY_STOP -> stopDiscovering()
            }
        }

        connectionRequestChannel.registerHost(this) {
            startConnecting(it)
        }

        communicationChannel.registerHost(this) {
            sendDataToEndpoint(it)
        }
    }

    private fun startAdvertising() {
        lifecycleScope.launch(backgroundDispatcher) {
            connectionManager.startAdvertising(requireNotNull(appSharedPreference.nickname))
                .collectOnMain {
                    when (it) {
                        is Success -> (it.data as? AdvertisingResult.ConnectionInitiated)?.let { data ->
                            acceptConnecting(data.endpointId)
                        }
                        is Failure -> startAdvertising()
                    }
                }
        }
    }

    private fun startDiscovering() {
        stopDiscovering()
        discoveryJob = lifecycleScope.launch(backgroundDispatcher) {
            connectionManager.startDiscovery()
                .collectOnMain {
                    when (it) {
                        is Success -> discoveryChannel.sendResult(it)
                        is Failure -> discoveryChannel.sendResult(it)
                    }
                }
        }
    }

    private fun stopDiscovering() {
        val previousDiscoveryJob = discoveryJob
        if (previousDiscoveryJob?.isActive == true) {
            previousDiscoveryJob.cancel()
            discoveryJob = null
        }
    }

    private fun startConnecting(endpointId: String) {
        stopDiscovering()
        lifecycleScope.launch(backgroundDispatcher) {
            connectionManager.connectRemote(endpointId)
                .collectOnMain {
                    when (it) {
                        is Success -> {
                            connectedEndpointId = endpointId
                            startListeningMessage()
                            connectionRequestChannel.sendResult(it)
                        }
                        is Failure -> connectionRequestChannel.sendResult(it)
                    }
                }
        }
    }

    private fun acceptConnecting(endpointId: String) {
        lifecycleScope.launch(backgroundDispatcher) {
            connectionManager.acceptRemote(endpointId)
                .collectOnMain {
                    when (it) {
                        is Success -> {
                            startListeningMessage()
                            withContext(Dispatchers.Main) {
                                ReceiveActivity.startOnHome(this@ConnectionService)
                            }
                        }
                        is Failure -> connectionRequestChannel.sendResult(it)
                    }
                }
        }
    }

    private fun startListeningMessage() {
        lifecycleScope.launch(backgroundDispatcher) {
            connectionManager.listenMessage()
                .collectOnMain {
                    when (it) {
                        is Success -> communicationChannel.sendRxData(it)
                        is Failure -> communicationChannel.sendRxData(it)
                    }
                }
        }
    }

    private fun sendDataToEndpoint(message: Message) {
        val connectedEndpointId = this.connectedEndpointId ?: return
        lifecycleScope.launch(backgroundDispatcher) {
            connectionManager.sendMessage(connectedEndpointId, message)
                .collect()
        }
    }
}