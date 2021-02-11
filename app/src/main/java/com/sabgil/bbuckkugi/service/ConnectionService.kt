package com.sabgil.bbuckkugi.service

import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.sabgil.bbuckkugi.common.Data.Failure
import com.sabgil.bbuckkugi.common.Data.Success
import com.sabgil.bbuckkugi.common.ext.collectOnMain
import com.sabgil.bbuckkugi.data.model.AdvertisingResult
import com.sabgil.bbuckkugi.data.model.Message
import com.sabgil.bbuckkugi.data.pref.AppSharedPreference
import com.sabgil.bbuckkugi.data.repository.ConnectionManager
import com.sabgil.bbuckkugi.presentation.ui.receive.ReceiveActivity
import com.sabgil.bbuckkugi.service.channel.CommunicationChannel
import com.sabgil.bbuckkugi.service.channel.ConnectionRequestChannel
import com.sabgil.bbuckkugi.service.channel.DiscoveryChannel
import com.sabgil.bbuckkugi.service.channel.DiscoveryChannel.Action.DISCOVERY_START
import com.sabgil.bbuckkugi.service.channel.DiscoveryChannel.Action.DISCOVERY_STOP
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
            connectionManager.startAdvertising(buildAdvertisingName())
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

    private fun buildAdvertisingName(): String {
        val name = requireNotNull(appSharedPreference.name)
        val gender = requireNotNull(appSharedPreference.gender).desc
        val profileImageUrl = appSharedPreference.profileImageUrl

        return if (profileImageUrl == null) {
            "$name|$gender"
        } else {
            "$name|$gender|$profileImageUrl"
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