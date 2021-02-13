package com.sabgil.bbuckkugi.data.repository

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.common.ext.offerFailure
import com.sabgil.bbuckkugi.data.model.DiscoveredEndpoint
import com.sabgil.bbuckkugi.data.model.Message
import com.sabgil.bbuckkugi.data.nearbyemitter.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class ConnectionManagerImpl @Inject constructor(val context: Context) : ConnectionManager {

    private val connectionsClient = Nearby.getConnectionsClient(context)
    private val advertisingResultEmitter = AdvertisingResultEmitter(SERVICED_ID, connectionsClient)

    override fun startAdvertising(hostName: String): Flow<Data<String>> =
        callbackFlow {
            advertisingResultEmitter.detachAdvertisingResultProducer()
            advertisingResultEmitter.attachAdvertisingResultProducer(this)
            advertisingResultEmitter.emit(hostName)
            awaitClose()
        }.onCompletion {
            connectionsClient.stopAdvertising()
        }

    override fun startDiscovery(): Flow<Data<DiscoveredEndpoint>> =
        callbackFlow {
            DiscoveryResultEmitter(SERVICED_ID, connectionsClient, this).emit()
            awaitClose()
        }.onCompletion {
            connectionsClient.stopDiscovery()
        }

    override fun connectRemote(endpointId: String): Flow<Data<Message>> =
        callbackFlow {
            ClientConnectionResultEmitter(
                endpointId,
                SERVICED_ID,
                connectionsClient,
                this
            ).emit()
            awaitClose()
        }.onCompletion {
            connectionsClient.stopAllEndpoints()
        }

    override fun acceptRemote(endpointId: String): Flow<Data<Message>> =
        callbackFlow {
            advertisingResultEmitter.attachConnectionResultProducer(this)
            HostConnectionResultEmitter(endpointId, connectionsClient, this).emit()
            awaitClose()
        }.onCompletion {
            advertisingResultEmitter.detachConnectionResultProducer()
            connectionsClient.stopAllEndpoints()
        }

    override fun sendMessage(endpointId: String, message: Message): Flow<Data<Nothing>> =
        callbackFlow {
            SendResultEmitter(endpointId, message, connectionsClient, this).emit()
            awaitClose()
        }

    companion object {

        private const val SERVICED_ID = "BBUCKKUGI"
    }
}