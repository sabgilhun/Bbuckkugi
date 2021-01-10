package com.sabgil.bbuckkugi.nearbycallback

import com.google.android.gms.nearby.connection.*
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.ConnectionRequest
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.isActive

class AdvertisingResultEmitter(
    private val hostName: String,
    private val serviceId: String,
    private val connectionsClient: ConnectionsClient,
    private val producerScope: ProducerScope<Result<ConnectionRequest>>
) {
    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(
            endpointId: String,
            connectionInfo: ConnectionInfo
        ) {
            producerScope.offer(
                Result.Success(ConnectionRequest(endpointId, connectionInfo.endpointName))
            )
        }

        override fun onConnectionResult(
            endpointId: String,
            resolution: ConnectionResolution
        ) {
            if (producerScope.isActive) {
                producerScope.close()
            }
        }

        override fun onDisconnected(endpointId: String) {
            if (producerScope.isActive) {
                producerScope.close()
            }
        }
    }

    fun emit() {
        connectionsClient.startAdvertising(
            hostName,
            serviceId,
            connectionLifecycleCallback,
            advertisingOptions
        ).addOnFailureListener {
            producerScope.offer(Result.Failure(it))
            producerScope.close()
        }
    }

    companion object {
        private val advertisingOptions = AdvertisingOptions
            .Builder()
            .setStrategy(Strategy.P2P_POINT_TO_POINT)
            .build()
    }
}