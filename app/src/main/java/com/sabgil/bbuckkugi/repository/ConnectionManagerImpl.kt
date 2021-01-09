package com.sabgil.bbuckkugi.repository

import android.content.Context
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.Strategy
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.ConnectionRequest
import com.sabgil.bbuckkugi.model.Data
import com.sabgil.bbuckkugi.model.DiscoveredEndpoint
import com.sabgil.bbuckkugi.nearbycallback.ConnectionCallback
import com.sabgil.bbuckkugi.nearbycallback.DiscoveryCallback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ConnectionManagerImpl @Inject constructor(val context: Context) : ConnectionManager {

    private val connectionsClient = Nearby.getConnectionsClient(context)

    private val advertisingOptions = AdvertisingOptions
        .Builder()
        .setStrategy(Strategy.P2P_POINT_TO_POINT)
        .build()

    private val discoveryOptions = DiscoveryOptions.Builder()
        .setStrategy(Strategy.P2P_POINT_TO_POINT)
        .build()

    override fun startAdvertise(hostName: String): Flow<Result<ConnectionRequest>> =
        callbackFlow {
            offer(Result.Loading)
            connectionsClient.startAdvertising(
                hostName,
                SERVICED_ID,
                ConnectionCallback(connectionsClient, this),
                advertisingOptions
            )
        }

    override fun startDiscovery(): Flow<Result<DiscoveredEndpoint>> {
        return callbackFlow {
            offer(Result.Loading)
            connectionsClient.startDiscovery(
                SERVICED_ID,
                DiscoveryCallback(this),
                discoveryOptions
            )
        }
    }

    override fun connectRemote(clientName: String, endpointId: String): Flow<Result<Data>> {
        TODO("Not yet implemented")
    }

    override fun acceptRemote(endpointId: String): Flow<Result<Data>> {
        TODO("Not yet implemented")
    }

    companion object {

        private const val SERVICED_ID = "BBUCKKUGI"
    }
}