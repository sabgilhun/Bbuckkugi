package com.sabgil.bbuckkugi.repository

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.sabgil.bbuckkugi.common.Data
import com.sabgil.bbuckkugi.model.ConnectionRequest
import com.sabgil.bbuckkugi.model.Message
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import com.sabgil.bbuckkugi.nearbyemitter.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class ConnectionManagerImpl @Inject constructor(val context: Context) : ConnectionManager {

    private val connectionsClient = Nearby.getConnectionsClient(context)

    override fun startAdvertising(hostName: String): Flow<Data<ConnectionRequest>> =
        callbackFlow {
            AdvertisingResultEmitter(hostName, SERVICED_ID, connectionsClient, this).emit()
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
            ClientConnectionResultEmitter(endpointId, SERVICED_ID, connectionsClient, this).emit()
            awaitClose()
        }.onCompletion {
            connectionsClient.stopAllEndpoints()
        }

    override fun acceptRemote(endpointId: String): Flow<Data<Message>> =
        callbackFlow {
            HostConnectionResultEmitter(endpointId, connectionsClient, this).emit()
            awaitClose()
        }.onCompletion {
            connectionsClient.stopAllEndpoints()
        }

    override fun sendData(endpointId: String, message: Message): Flow<Data<Nothing>> =
        callbackFlow {
            SendResultEmitter(endpointId, message, connectionsClient, this).emit()
            awaitClose()
        }

    companion object {
        private const val SERVICED_ID = "BBUCKKUGI"
    }
}