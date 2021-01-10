package com.sabgil.bbuckkugi.nearbycallback

import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.model.ConnectionRequest
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.isActive

class HostConnectionCallback(
    private val producerScope: ProducerScope<Result<ConnectionRequest>>
) : ConnectionLifecycleCallback() {

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