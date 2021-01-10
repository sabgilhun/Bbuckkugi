package com.sabgil.bbuckkugi.repository

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.ConnectionRequest
import com.sabgil.bbuckkugi.model.Data
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import com.sabgil.bbuckkugi.nearbycallback.AdvertisingResultEmitter
import com.sabgil.bbuckkugi.nearbycallback.ClientConnectionResultEmitter
import com.sabgil.bbuckkugi.nearbycallback.DiscoveryResultEmitter
import com.sabgil.bbuckkugi.nearbycallback.HostConnectionResultEmitter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ConnectionManagerImpl @Inject constructor(val context: Context) : ConnectionManager {

    private val connectionsClient = Nearby.getConnectionsClient(context)

    override fun startAdvertising(hostName: String): Flow<Result<ConnectionRequest>> =
        callbackFlow {
            offer(Result.Loading)
            AdvertisingResultEmitter(hostName, SERVICED_ID, connectionsClient, this).emit()
        }

    override fun startDiscovery(): Flow<Result<DiscoveredEndpoint>> =
        callbackFlow {
            offer(Result.Loading)
            DiscoveryResultEmitter(SERVICED_ID, connectionsClient, this).emit()
        }

    override fun connectRemote(clientName: String, endpointId: String): Flow<Result<Data>> =
        callbackFlow {
            offer(Result.Loading)
            ClientConnectionResultEmitter(endpointId, SERVICED_ID, connectionsClient, this).emit()
        }

    override fun acceptRemote(endpointId: String): Flow<Result<Data>> =
        callbackFlow {
            offer(Result.Loading)
            HostConnectionResultEmitter(endpointId, connectionsClient, this).emit()
        }

    companion object {
        private const val SERVICED_ID = "BBUCKKUGI"
    }
}