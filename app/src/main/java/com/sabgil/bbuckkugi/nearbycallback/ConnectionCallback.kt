package com.sabgil.bbuckkugi.nearbycallback

import com.google.android.gms.nearby.connection.*
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes.STATUS_OK
import com.sabgil.bbuckkugi.common.Result
import com.sabgil.bbuckkugi.eception.ConnectionException
import com.sabgil.bbuckkugi.model.ConnectionRequest
import kotlinx.coroutines.channels.ProducerScope

class ConnectionCallback(
    private val connectionClient: ConnectionsClient,
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
        when (resolution.status.statusCode) {
            STATUS_OK -> sendAcceptData(endpointId)
            STATUS_CONNECTION_REJECTED -> sendRejectData(endpointId)
            else -> producerScope.offer(Result.Failure(ConnectionException()))
        }
        producerScope.close()
    }

    override fun onDisconnected(endpointId: String) {
        producerScope.close()
    }

    private fun sendAcceptData(endpointId: String) {
        connectionClient.sendPayload(
            endpointId,
            Payload.fromBytes(ConnectionCheck.Accept.toBytes())
        )
    }

    private fun sendRejectData(endpointId: String) {
        connectionClient.sendPayload(
            endpointId,
            Payload.fromBytes(ConnectionCheck.Reject.toBytes())
        )
    }
}